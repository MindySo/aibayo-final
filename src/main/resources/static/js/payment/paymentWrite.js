let afterSuccessType = null;
let discountList = [];
let memoList = [];
let price = 0;

$(document).ready(function() {

    // 전체 선택
    $("#checkAll").click(function() {
        $("input.class_checkbox").prop("checked", this.checked);
    });

    // 전체 선택 빼기
    $("input.class_checkbox").click(function() {
        let total = $("input.class_checkbox").length;
        let checked = $("input.class_checkbox:checked").length;

        if (checked === total) {
            $("#checkAll").prop("checked", true);
        } else {
            $("#checkAll").prop("checked", false);
        }
    });


    document.querySelectorAll('.inputtext').forEach(function(inputElement) {
        // input 이벤트 리스너: 입력된 값에서 숫자가 아닌 문자를 제거
        inputElement.addEventListener('input', function (e) {
            this.value = this.value.replace(/[^0-9]/g, '');

            // #discount_rate인 경우 숫자 두 자리까지만 허용
            if (this.id === 'discount_rate' && this.value.length > 2) {
                this.value = this.value.slice(0, 2);
            }
        });

        // keydown 이벤트 리스너: 숫자키와 허용된 키만 입력 가능하게 설정
        inputElement.addEventListener('keydown', function (e) {
            const allowedKeys = [
                'Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete', 'Enter'
            ];

            if (
                (e.key >= '0' && e.key <= '9') || // 숫자키 허용
                allowedKeys.includes(e.key)       // 허용된 기타 키 허용
            ) {
                return;
            } else {
                e.preventDefault(); // 허용되지 않은 키 입력 차단
            }
        });
    });

    showSelectAll();
    $("#classSelect").on('change', function() {
        showSelectAll();
    });


    $(document).on('change', '.selectoption-child', function () {
        let classNo = $('.selectoption-child').val()

        let param = {
            classNo: classNo === "" ? null : classNo
        };

        let url = "/payment/admin/paymentBillingWriteGetKid";

        afterSuccessType = 'getKid'
        commonAjax(url, 'POST', param);

    });


    $(document).on('click', '#setIdList', function () {
        let classNo = $('.selectClass').val();
        let className = $('.selectClass option:selected').text();

        // member id 리스트로
        let selectedKidList = [];
        let selectedOptions = document.querySelectorAll('.form-check .class_checkbox');
        selectedOptions.forEach(function(option) {
            if (option.checked) {
                let selectedKid = {
                    id: option.value,
                    memberName: option.getAttribute('data-member-name'),
                    kidNo: option.getAttribute('data-kid-no'),
                    kidName: option.getAttribute('data-kid-name')
                };
                selectedKidList.push(selectedKid);
            }
        });

        let param = {
            classNo : classNo,
            className : className,
            selectedKidList : selectedKidList
        };

        console.log(param);

        let url = "/payment/admin/ShowSelectedKid";

        afterSuccessType = 'showSelectedKid';
        commonAjax(url, 'POST', param);

    });

    $(document).on('blur', '#payment_price', function () {
        price = parseFloat($(this).val());
        let showPrice = formatPrice(price);
        $(this).val(showPrice);

        calDiscount();
    });

    $(document).on('focus', '#payment_price', function () {
        // 원래 숫자 값을 필드에 다시 넣음
        $(this).val(price);
    });

    $(document).on('click', '#add_discount', function () {

        // 새 할인 항목을 list에 추가
        let newDiscountElement = {
            kidNo: $('.discountForSelectedKids option:selected').attr('data-kid-no'),
            kidName: $('.discountForSelectedKids option:selected').text(),
            id: $('.discountForSelectedKids option:selected').attr('id'),
            discountRate: $('#discount_rate').val()
        };
        discountList.push(newDiscountElement);

        let param = {
            discountList : discountList
        };

        console.log(discountList);

        let url = "/payment/admin/showDiscountedKid";


        $('.discountForSelectedKids').prop('selectedIndex', 0);
        $('#discount_rate').val('');

        afterSuccessType = 'showDiscountedKid';
        commonAjax(url, 'POST', param);

    });

    $(document).on('click', '#add_memo', function () {

        // 새 할인 항목을 list에 추가
        let newMemoElement = {
            kidNo: $('.memoForSelectedKids option:selected').attr('data-kid-no'),
            kidName: $('.memoForSelectedKids option:selected').text(),
            id: $('.memoForSelectedKids option:selected').attr('id'),
            paymentMemo: $('.memo-textarea').val()
        };
        memoList.push(newMemoElement);

        let param = {
            memoList : memoList
        };

        console.log(memoList);

        let url = "/payment/admin/showMemo";

        $('.memoForSelectedKids').prop('selectedIndex', 0);
        $('.memo-textarea').val('');

        afterSuccessType = 'showMemo';
        commonAjax(url, 'POST', param);

    });


    $('.timepicker').timepicker({
        timeFormat: 'HH:mm',
        interval: 60,
        minTime: '0',
        maxTime: '23',
        dynamic: false,
        dropdown: true,
        scrollbar: true,
        defaultTime: '23'
    }).on('selectTime', function() {
        var openTime = $(this).val();
        console.log('오픈 시간 : ' + openTime);
    });


    $(document).on('click', '#send_bill', function () {
        let billList = [];

        // $('.selectedchild .child').each(function(index, element) {
        //     let id = $(this).attr('id');
        //     let name = $(this).find('.parentNameBox').attr('data-parent-name');
        //     let kidNo = $(this).find('.kidNameBox').attr('id');
        //     let kidName = $(this).find('.kidNameBox').text();
        //     let classNo = $('.selectedClassName').attr('value');
        //     let className = $('.selectedClassName').text();
        //     let paymentTitle = $('#paymentTitle').val();
        //     let discountRate = getDiscount(kidNo);
        //     let paymentPrice = price;
        //     let paymentEndDate = ($('.datepicker').val() + 'T' + $('.timepicker').val()).replace(/\./g, '-');
        //     let paymentMemo = getMemo(kidNo);
        //
        //     let paymentDto = {id, name, kidNo, kidName, classNo, className, paymentTitle, discountRate, paymentPrice, paymentEndDate, paymentMemo};
        //     billList.push(paymentDto);
        // });

        $('.selectedchild .child').each(function(index, element) {
            let id = $(this).attr('id');
            let kidNo = $(this).find('.kidNameBox').attr('id');
            let classNo = $('.selectedClassName').attr('value');
            let paymentTitle = $('#paymentTitle').val();
            let discountRate = getDiscount(kidNo);
            let paymentPrice = price;
            let paymentEndDate = ($('.datepicker').val() + 'T' + $('.timepicker').val()).replace(/\./g, '-');
            let paymentMemo = getMemo(kidNo);
            let kinderNo = $('.title').attr('value');

            let paymentDto = {id, kidNo, classNo, paymentTitle, discountRate, paymentPrice, paymentEndDate, paymentMemo, kinderNo};
            billList.push(paymentDto);
        });

        let param = {
            billList : billList
        };

        console.log(param);

        let url = "/payment/admin/sendBill";
        afterSuccessType = 'sendBill';
        commonAjax(url, 'POST', param);

    });





});

// textarea 사이즈 자동조절
function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// textarea 글자수 제한
function chkword(obj, maxWord) {

    var strValue = obj.value;
    var strLen = strValue.length;
    var totalByte = 0;
    var len = 0;
    var oneChar = "";
    var str2 = "";

    for (var i = 0; i < strLen; i++) {
        oneChar = strValue.charAt(i);
        if (escape(oneChar).length > 4) {
            totalByte += 2;
        } else {
            totalByte++;
        }

        // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
        if (totalByte <= maxWord) {
            // len = i + 1;
            len = maxWord;
        }
    }

    // 넘어가는 글자는 자른다.
    if (strLen > maxWord) {
        alert(maxWord + "자를 초과 입력 할 수 없습니다.");
        str2 = strValue.substr(0, len);
        obj.value = str2;
        strLen = maxWord
        chkword(obj, maxWord);
    }

    $('#textcnt').html("( " + strLen + " / " + maxWord + " )");
}

function showSelectAll(){
    const selectedValue = $("#classSelect").val();
    console.log('selectedValue :' + selectedValue);
    if (selectedValue === '-1000') {
        document.querySelector('#noClass').style.display = 'flex';
        document.querySelector('#selectAllClass').style.display = 'none';
    } else {
        document.querySelector('#noClass').style.display = 'none';
        document.querySelector('#selectAllClass').style.display = 'flex';
    }
}

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function calDiscount(){
    $('#discountList .discountElement').each(function(index, element) {

        let discountRate = $(element).data('discount-rate');
        console.log('Discount Rate for element', index, ':', discountRate);

        let discountedPrice = price * (100 - discountRate) * 0.01;

        $(element).find('.originalPrice').text(formatPrice(price)); // 원래 가격을 설정
        $(element).find('.discountedPrice').text(formatPrice(Math.round(discountedPrice))); // 할인된 가격을 설정
    });
}

function getDiscount(kidNo) {
    // .discountElement 중에서 data-kid-no 속성이 kidNo와 일치하는 요소를 찾음
    let matchingElement = $('.discountElement').filter(function() {
        return $(this).data('kid-no') == kidNo;
    });

    // 일치하는 요소가 있으면 그 요소의 data-kid-no 값을 가져옴
    if (matchingElement.length > 0) {
        let dataDiscountRate = matchingElement.data('discount-rate');
        console.log('Matching discount-rate:', dataDiscountRate);
        return dataDiscountRate;
    } else {
        console.log('No matching element found');
        return null;
    }
}

function getMemo(kidNo) {
    // .memoElement 중에서 data-kid-no 속성이 kidNo와 일치하는 요소를 찾음
    let matchingElement = $('.memoElement').filter(function() {
        return $(this).data('kid-no') == kidNo;
    });

    // 일치하는 요소가 있으면 그 요소의 data-kid-no 값을 가져옴
    if (matchingElement.length > 0) {
        let dataPaymentMemo = matchingElement.data('payment-memo');
        console.log('Matching payment-memo:', dataPaymentMemo);
        return dataPaymentMemo;
    } else {
        console.log('No matching element found');
        return null;
    }
}

function afterSuccess(response, method) {

    if(afterSuccessType === 'getKid'){
        $('.carousel').replaceWith($(response).find('.carousel'));
        afterSuccessType = null;
    }

    if(afterSuccessType === 'showSelectedKid'){
        $('.div-2').replaceWith($(response).find('.div-2'));
        $('.discountForSelectedKids').replaceWith($(response).find('.discountForSelectedKids'));
        $('.memoForSelectedKids').replaceWith($(response).find('.memoForSelectedKids'));
        $('#discountList').replaceWith($(response).find('#discountList'));
        $('#memoList').replaceWith($(response).find('#memoList'));

        afterSuccessType = 'null';
    }

    if(afterSuccessType === 'showDiscountedKid'){
        $('#discountList').replaceWith($(response).find('#discountList'));
        calDiscount();
        afterSuccessType = 'null';
    }

    if(afterSuccessType === 'showMemo'){
        $('#memoList').replaceWith($(response).find('#memoList'));
        afterSuccessType = 'null';
    }

    if(afterSuccessType === 'sendBill'){
        Swal.fire({
            title: "청구 완료",
            text: "창을 닫으면 청구/결제 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            window.location.href = window.location.origin + '/payment/admin/paymentBillingMain';
        });
    }



}