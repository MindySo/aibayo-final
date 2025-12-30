let billNo;

$(document).ready(function() {
    const rows = document.querySelectorAll('.tuples .tuple');

    rows.forEach(row => {
        row.addEventListener('click', function() {
            document.getElementById("detailView").style.display = "flex";

            // Extract the billNo from the row's value attribute
            billNo = row.getAttribute('value');

            let param = {
                billNo : billNo
            };

            console.log("param:" + JSON.stringify(param));

            let url = "/payment/user/paymentDetail";

            commonAjax(url, 'POST', param);
        });
    });
});

document.addEventListener('click', function(event) {
    if (event.target && event.target.id === 'xButton') {
        document.getElementById("detailView").style.display = "none";
    }
});

document.addEventListener('click', function(event) {
    if (event.target && event.target.id === 'payBill') {
        window.location.href = '/payment/user/paymentPay?billNo=' + billNo;
    }
});


function detailFormatDateTime(input) {
    if(input === null){
        return '-';
    }else{
        // 날짜와 시간 분리
        var dateTimeParts = input.split('T');
        var datePart = dateTimeParts[0]; // '2024-06-02'
        var timePart = dateTimeParts[1]; // '06:00:00.000000'
        var timePartTrim = timePart.split('.');
        var time = timePartTrim[0]; // '06:00:00'

        // 날짜 형식을 'YYYY.MM.DD'로 변경
        var formattedDate = datePart.replace(/-/g, '.'); // '2024.06.02'

        // 줄바꿈 추가하여 최종 결과 반환
        return formattedDate + '&nbsp' + time;
    }
}


function formatize() {
    document.querySelectorAll('.detail_payment_status').forEach(function (element) {
        var paymentStatus = element.dataset.paymentStatus;

        if (paymentStatus == 0) {
            paymentStatus = '청구';
        } else if (paymentStatus == 1) {
            paymentStatus = '수납';
        } else if (paymentStatus == 2) {
            paymentStatus = '미납';
        } else if (paymentStatus == 3) {
            paymentStatus = '발행취소';
        } else if (paymentStatus == 4) {
            paymentStatus = '승인취소';
        }

        element.textContent = paymentStatus;
    });

    document.querySelectorAll('.detail_payment_log_reg_date').forEach(function (element) {
        var regDate = element.dataset.regDate;
        element.innerHTML = detailFormatDateTime(regDate);
    });

    document.querySelectorAll('.detail_payment_start_date').forEach(function (element) {
        var startDate = element.dataset.startDate;
        element.innerHTML = detailFormatDateTime(startDate);
    });

    document.querySelectorAll('.detail_payment_end_date').forEach(function (element) {
        var endDate = element.dataset.endDate;
        element.innerHTML = detailFormatDateTime(endDate);
    });

    document.querySelectorAll('.detail_payment_price').forEach(function (element) {
        var paymentPrice = element.dataset.paymentPrice;
        // 숫자를 문자열로 변환하고, 천 단위로 콤마를 추가
        var formattedAmount = paymentPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        element.innerHTML = formattedAmount + '원';
    });

    document.querySelectorAll('.detail_final_price').forEach(function (element) {
        var paymentPrice = element.dataset.paymentPrice;
        var discountRate = element.dataset.discountRate;
        // 숫자를 문자열로 변환하고, 천 단위로 콤마를 추가
        var formattedAmount = (paymentPrice * (100 - discountRate) * 0.01)
            .toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        element.innerHTML = formattedAmount + '원';
    });
}

function afterSuccess(response) {
    $('.modal-content').replaceWith($(response).find('.modal-content'));

        // 모든 tr 태그 내의 detail_payment_status 클래스가 있는 td 요소를 선택
        const paymentStatusElements = document.querySelectorAll('.detail_payment_status');
        let status = [];
        let shouldHideButton = false;

        // 모든 요소에 대해 반복
        paymentStatusElements.forEach(function(element) {
            const paymentStatus = element.getAttribute('data-payment-status');
            status.push(paymentStatus);
        });
            // 만약 paymentStatus 값이 1, 2, 3, 4 중 하나와 일치하면
        if (status.includes('1') || status.includes('2') || status.includes('3') || status.includes('4')) {
            shouldHideButton = true;
        }

        // 조건이 충족되면 payBill 버튼의 display 속성을 null로 설정
        if (shouldHideButton) {
            document.getElementById('payBill').style.display = 'none';
        }


    formatize();
}

