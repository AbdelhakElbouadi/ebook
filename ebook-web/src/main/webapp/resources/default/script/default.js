/* global TCO */

var tokenInput = document.getElementById('token');

var successCallback = function(data) {
    var paymentForm = document.getElementById('paymentForm');
    console.log(data.response.token);
    var result = [
        {name : "status", value : "success"},
        {name: "token", value: data.response.token.token}
    ];
    getPaymentToken(result);
    //paymentForm.submit();
};

var errorCallback = function(data) {
    var result = [
        {name : "status", value : "error"},
        {name : "errorCode", value : data.errorCode},
        {name : "errorMsg", value : data.errorMsg}
    ];
    getPaymentToken(result);
    
    if(data.errorCode === 200){
        alert("Unable To Process The Request");
        tokenRequest();
    } else if(data.errorCode === 300){
        alert("Seller Unauthorized");
    }else if(data.errorCode === 400){
        alert("Bad request - parameter error");
    }else if(data.errorCode === 401){
        alert("Bad request - missing data");
    }else if(data.errorCode === 500){
        alert("Request failed");
    }
};

function tokenRequest(){
    //alert("Alert the Token Request");
    console.log("I have being called from javascript so prepare for war");
    var args = {
        sellerId : '901388955',
        publishableKey : '8275213E-B2E7-4800-8F11-F6632CA0349E',
        ccNo : $('#paymentForm\\:ccNo').val(),
        cvv : $('#paymentForm\\:cvv').val(),
        expMonth : $('#paymentForm\\:expDate').val().split('/')[0],
        expYear : $('#paymentForm\\:expDate').val().split('/')[1]
    };
    console.log(args);
    
    TCO.requestToken(successCallback, errorCallback, args);
}


$(document).ready(function (){
    $('#paymentForm\\:expDate').monthpicker();
    
    //Pull in the public encryption key for our environment
    TCO.loadPubKey('sandbox');
});






