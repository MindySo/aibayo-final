$(document).ready(function() {

    let endDate = $('.endDate').text();
    let formattedDateTime = endDate.replace('T', ' ');
    $('.endDate').text(formattedDateTime);

    let paymentPrice = parseInt($('.paymentPrice').text().trim(), 10);
    $('.paymentPrice').text(formatPrice(paymentPrice));

    let finalPrice = parseInt($('.finalPrice').text().trim(), 10);
    $('.finalPrice').text(formatPrice(finalPrice));


    const generateRandomString = () =>
        window.btoa(Math.random()).slice(0, 20);
    var amount =  parseInt($('.finalPrice').text().trim(), 10);
    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
    const customerKey = generateRandomString();
    const paymentWidget = PaymentWidget(clientKey, customerKey);

    const modal = document.getElementById("paymentModal");
    const btn = document.getElementById("openModalButton");
    const span = document.getElementsByClassName("close")[0];

    btn.onclick = function () {
        modal.style.display = "block";

        console.log($('.paymentPrice').text().trim());
        console.log($('.paymentTitle').text().trim());

    };

    span.onclick = function () {
        modal.style.display = "none";
    };

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };

    // 결제 UI 렌더링
    paymentMethodWidget = paymentWidget.renderPaymentMethods(
        "#payment-method",
        { value: amount },
        { variantKey: "DEFAULT" }
    );
    // 이용약관 UI 렌더링
    paymentWidget.renderAgreement("#agreement", { variantKey: "AGREEMENT" });

    // '결제하기' 버튼 누르면 결제창 띄우기
    const button = document.getElementById("payment-button");
    let billNo = $('.paymentContent').val();
    button.addEventListener("click", function () {
        paymentWidget.requestPayment({
            orderId: generateRandomString(),
            orderName: $('.paymentTitle').text().trim(),
            successUrl: window.location.origin + "/payment/user/success/?billNo=" + billNo,
            failUrl: window.location.origin + "/payment/user/fail",
        });
    });
});

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}