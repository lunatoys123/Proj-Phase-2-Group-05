<?php

$servername = "cslinux0.comp.hkbu.edu.hk";
$username = "comp4107_grp05";
$password = "503830";
$dbname = "comp4107_grp05";

// Create connection
$conn = new mysqli( $servername, $username, $password, $dbname );

// Check connection
if ( $conn->connect_error ) {
    die( "Connection failed: " . $conn->connect_error );
}
$req = json_decode( $_POST["BAMSReq"], false );

if ( strcmp( $req->msgType, "LoginReq" ) === 0 ) {
    $reply->msgType = "LoginReply";
    $reply->cardNo = $req->cardNo;
    $reply->pin = $req->pin;
    $sql = "select count(*) as counter from cardMaster where cardID='".$req->cardNo."'";
    $result = $conn->query( $sql );
    $reply->counter = "";

    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->counter = $row["counter"];
        }
    }

    if ( $reply->counter == 1 ) {
        $sql2 = "select count(*) as counter from cardMaster where cardID= '".$req->cardNo."' and pin='".$req->pin."'";
        $result = $conn->query( $sql2 );

        if ( $result->num_rows>0 ) {
            while( $row = $result->fetch_assoc() ) {
                $reply->counter = $row["counter"];
            }

        }

        if ( $reply->counter == 1 )$reply->cred = "Credential";
        else $reply->cred = "Error Login";

    } else {
        $reply->cred = "Wrong card";
    }

} else if ( strcmp( $req->msgType, "GetAccReq" ) === 0 ) {
    $reply->msgType = "GetAccReply";
    $reply->cardNo = $req->cardNo;
    $reply->cred = $req->cred;
    $reply->accounts = "";
    $sql = "select cta.Account_ID, acc.type from cardToAccount cta ".
    "join Account acc on cta.Account_ID=acc.Account_ID ".
    "where cta.cardID='".$req->cardNo."' ".
    "order by acc.type asc";
    $result = $conn->query( $sql );

    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->accounts .= $row["Account_ID"]."(".$row["type"].") /";
        }
    }

} else if ( strcmp( $req->msgType, "WithdrawReq" ) === 0 ) {
    $reply->msgType = "WithdrawReply";
    $reply->cardNo = $req->cardNo;
    $reply->accNo = $req->accNo;
    $reply->cred = $req->cred;

    $reply->amount = $req->amount;
    $reply->counter = "";

    $sql = "select count(*) as counter from Account where Account_ID='".$req->accNo."' ".
    "and Total_Amount>=".doubleval( $req->amount );
    $result = $conn->query( $sql );
    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->counter = $row["counter"];
        }
    }
    $reply->msgType = "WithdrawReply";
    $reply->cardNo = $req->cardNo;
    $reply->accNo = $req->accNo;
    $reply->cred = $req->cred;

    $reply->amount = $req->amount;
    $reply->counter = "";

    $sql = "select count(*) as counter from Account where Account_ID='".$req->accNo."' ".
    "and Total_Amount>=".doubleval( $req->amount );
    $result = $conn->query( $sql );
    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->counter = $row["counter"];
        }
    }
    $reply->outAmount = "";
    if ( $reply->counter == 1 ) {
        $sql = "update Account set Total_Amount=Total_Amount-".intval( $req->amount ).
        " where Account_ID='".$req->accNo."'";

        if ( $conn->query( $sql ) === TRUE ) {
            $reply->outAmount = $req->amount;
        } else {
            $reply->outAmount = doubleval( "0" );
        }
    } else {
        $reply->outAmount = -1;
    }

} else if ( strcmp( $req->msgType, "DepositReq" ) === 0 ) {
    $reply->msgType = "DepositReply";
    $reply->cardNo = $req->cardNo;
    $reply->accNo = $req->accNo;
    $reply->cred = $req->cred;
    $reply->amount = $req->amount;
    $reply->depAmount = $req->amount;
    $sql = "update Account set Total_Amount=Total_Amount+".intval( $req->amount ).
    " where Account_ID='".$req->accNo."'";
    $reply->confirmUpdate = "";

    if ( $conn->query( $sql ) === TRUE ) {
        $reply->confirmUpdate = "Deposit succssful";
    } else {
        $reply->depAmount = intval( "0" );
    }
} else if ( strcmp( $req->msgType, "EnquiryReq" ) === 0 ) {
    $reply->msgType = "EnquiryReply";
    $reply->cardNo = $req->cardNo;
    $reply->accNo = $req->accNo;
    $reply->cred = $req->cred;
    $reply->amount = "";
    $sql = "select acc.Total_Amount as Total_Amount from cardToAccount cta ".
    "join Account acc on cta.Account_ID=acc.Account_ID ".
    "join cardMaster cm on cm.cardID=cta.cardID ".
    "where 1=1 ".
    "and cta.cardID='".$req->cardNo."'".
    "and cta.Account_ID='".$req->accNo."'";

    $result = $conn->query( $sql );

    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->amount .= $row["Total_Amount"]." ";
        }
    }

} else if ( strcmp( $req->msgType, "TransferReq" ) === 0 ) {
    $reply->msgType = "TransferReply";
    $reply->cardNo = $req->cardNo;
    $reply->cred = $req->cred;
    $reply->fromAcc = $req->fromAcc;
    $reply->toAcc = $req->toAcc;
    $reply->amount = $req->amount;
    $reply->counter = "";
    $sql = "select count(*) as counter from Account where Account_ID='".$req->fromAcc."' and Total_Amount>=".intval( $req->amount );
    $result = $conn->query( $sql );
    if ( $result->num_rows>0 ) {
        while( $row = $result->fetch_assoc() ) {
            $reply->counter = $row["counter"];
        }
    }

    if ( $reply->counter == 1 ) {

        $sql1 = "update Account set Total_Amount=Total_Amount-".intval( $req->amount ).
        " where Account_ID='".$req->fromAcc."'";

        $reply->result1 = "";

        if ( $conn->query( $sql1 ) === TRUE ) {
            $reply->result1 = "Successful";
        } else {
            $reply->result1 = "fail";
        }

        $sql2 = "update Account set Total_Amount=Total_Amount+".intval( $req->amount ).
        " where Account_ID='".$req->toAcc."'";

        $reply->result2 = "";

        if ( $conn->query( $sql2 ) ) {
            $reply->result2 = "Successful";
        } else {
            $reply->result2 = "fail";
        }

        $reply->transAmount = $req->amount;
    } else {
        $reply->transAmount = -1;

    }
}
$conn->close();
echo json_encode( $reply );
?>