$(document).ready(function() {

    // 쿼리 파라미터 값이 결제 요청할 때 보낸 데이터와 동일한지 반드시 확인하세요.
    // 클라이언트에서 결제 금액을 조작하는 행위를 방지할 수 있습니다.
    const urlParams = new URLSearchParams(window.location.search);

    // 서버로 결제 승인에 필요한 결제 정보를 보내세요.
    async function confirm() {
    var requestData = {
    paymentKey: urlParams.get("paymentKey"),
    orderId: urlParams.get("orderId"),
    amount: urlParams.get("amount"),
    billNo: urlParams.get("billNo")  // URL에서 billNo 가져옴
};

    const response = await fetch("/confirm", {
    method: "POST",
    headers: {
    "Content-Type": "application/json",
},
    body: JSON.stringify(requestData),
});

    const json = await response.json();

    if (!response.ok) {
    // TODO: 결제 실패 비즈니스 로직을 구현하세요.
    console.log(json);
    window.location.href = `/fail?message=${json.message}&code=${json.code}`;
}

    // TODO: 결제 성공 비즈니스 로직을 구현하세요.
    console.log(json);
}
    confirm();

    const paymentKeyElement = document.getElementById("paymentKey");
    const orderIdElement = document.getElementById("orderId");
    const amountElement = document.getElementById("amount");

    orderIdElement.textContent = "주문번호: " + urlParams.get("orderId");
    amountElement.textContent = "결제 금액: " + urlParams.get("amount");
    paymentKeyElement.textContent =
    "paymentKey: " + urlParams.get("paymentKey");

});