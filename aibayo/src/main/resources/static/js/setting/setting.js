$(document).ready(function() {
    $('#open').timepicker({
        timeFormat: 'h:mm p',
        interval: 10,
        minTime: '6',
        maxTime: '10',
        defaultTime: '9',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    }).on('selectTime', function() {
        var openTime = $(this).val(); // 선택된 시간 값
        console.log('오픈 시간 : ' + openTime);
    });

    $('#close').timepicker({
        timeFormat: 'h:mm p',
        interval: 10,
        minTime: '13:00pm',
        maxTime: '22:00pm',
        defaultTime: '18:00pm',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    }).on('selectTime', function() {
        var closeTime = $(this).val(); // 선택된 시간 값
        console.log('하원 시간: ' + closeTime);
    });

    $('#addBtn').on('click', function(event) {
        console.log('저장버튼 클릭됨');
        event.preventDefault(); // 기본 폼 제출 방지

        // 시간 문자열에서 AM/PM 제거 및 24시간 형식으로 변환
        function convertTo24Hour(time) {
            let [hours, minutes, period] = time.split(/[: ]/);

            // AM/PM 변환
            if (period === 'PM' && hours !== '12') {
                hours = parseInt(hours, 10) + 12;
            } else if (period === 'AM' && hours === '12') {
                hours = '00';
            }

            // 두 자리로 포맷팅
            if (hours.length === 1) {
                hours = '0' + hours;
            }

            // 두 자리로 포맷팅
            if (minutes.length === 1) {
                minutes = '0' + minutes;
            }

            return `${hours}:${minutes}`;
        }

        const kinderPostCode = $('#sample6_postcode').val();
        const addr = $('#sample6_address').val();
        const detailAddrRaw = $('#sample6_detailAddress').val();
        const detailAddr = detailAddrRaw ? ' ' + detailAddrRaw : ''; // 공백 추가

        const kinderName = $('#kinderName').val().trim(); // 공백 제거

        const kinderLocNo = $('#locNo').val();
        const kinderMidNo = $('#midNo').val();
        const kinderEndNo = $('#endNo').val();

        const openTimeRaw = $('#open').val();
        const closeTimeRaw = $('#close').val();

        const kinderOpenTime = convertTo24Hour(openTimeRaw); // 24시간 형식으로 변환
        const kinderCloseTime = convertTo24Hour(closeTimeRaw); // 24시간 형식으로 변환

        const sggList = $('#sample6_sigunguCode').val();
        const sidoList = sggList.substring(0, 2);

        // 값 체크
        if (kinderPostCode.length === 0) {
            console.log("우편번호 입력 누락");
            alert("우편번호를 입력해주세요.");
            $('#sample6_postcode').focus();
            return false;
        }
        if (detailAddr.length === 0) {
            console.log("상세 주소 입력 누락");
            if (!confirm("상세 주소 입력이 되지 않았습니다. 이대로 진행하시겠습니까?")) {
                $('#sample6_detailAddress').focus();
                return false;
            }
        }
        if (kinderName.length === 0) {
            console.log("유치원 이름 입력 누락");
            alert("유치원 이름을 입력해주세요.");
            $('#kinderName').focus();
            return false;
        }
        if (/[a-zA-Z]/.test(kinderName)) {
            console.log("유치원 이름 형식 오류");
            alert("유치원 이름은 한글만 입력할 수 있습니다.");
            $('#kinderName').focus();
            return false;
        }

        // 전화번호 형식 체크
        if (!/^\d+$/.test(kinderMidNo) || !/^\d+$/.test(kinderEndNo)) {
            console.log("전화번호 입력 오류");
            alert("전화번호는 숫자만 입력할 수 있습니다.");
            if (!/^\d+$/.test(kinderMidNo)) {
                $('#midNo').focus();
                return false;
            }
            if (!/^\d+$/.test(kinderEndNo)) {
                $('#endNo').focus();
                return false;
            }
        }
        if (kinderMidNo.length === 0) {
            console.log("미들넘버 입력 누락");
            alert("전화번호의 중간 번호를 입력해주세요.");
            $('#midNo').focus();
            return false;
        }
        if (kinderMidNo.length !== 3 && kinderMidNo.length !== 4) {
            console.log("미들넘버 길이 오류");
            alert("올바른 형식의 번호를 입력해주세요.");
            $('#midNo').focus();
            return false;
        }
        if (kinderEndNo.length === 0) {
            console.log("엔드넘버 입력 누락");
            alert("전화번호의 끝 번호를 입력해주세요.");
            $('#endNo').focus();
            return false;
        }
        if (kinderEndNo.length !== 4) {
            console.log("엔드넘버 길이 오류");
            alert("올바른 형식의 번호를 입력해주세요.");
            $('#endNo').focus();
            return false;
        }

        const kinderData = {
            kinderPostCode: kinderPostCode,
            kinderAddr: addr,
            kinderAddrDetail :detailAddr,
            kinderName: kinderName,
            kinderLocNo : kinderLocNo,
            kinderMidNo : kinderMidNo,
            kinderEndNo : kinderEndNo,
            kinderOpenTime: kinderOpenTime,
            kinderCloseTime: kinderCloseTime,
            sggList:sggList,
            sidoList:sidoList,
        };

        console.log('kinderData:', kinderData); // 객체 출력으로 데이터 확인
        let url = '/setting/addOk';

        // commonAjax 호출 시 afterSuccess를 인자로 전달
        commonAjax(url, 'POST', kinderData, afterSuccess);
    });

    // 주소 검색 함수 전역 범위로 이동
    window.sample6_execDaumPostcode = function() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = '';
                var extraAddr = '';

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }

                } else {
                    document.getElementById("sample6_extraAddress").value = '';
                }

                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                document.getElementById("sample6_detailAddress").focus();

                // sigunguCode를 폼에 추가
                document.getElementById('sample6_sigunguCode').value = data.sigunguCode || ''; // sigunguCode가 있을 때만 설정
            }
        }).open();
    };


});


// afterSuccess 정의 및 호출
function afterSuccess(response) {
    console.log("유치원 세팅");

    // hidden 필드에서 username 값을 가져옵니다.
    const username = $('input[name="username"]').val();
    const kinderNo = response.kinderNo; // 유치원 등록에 성공한 후 받은 kinderNo 값이라고 가정

    console.log("aftersucess username : " + username);
    console.log("aftersucess kinderNo : " + kinderNo);

    // 서버에 데이터 전송 (URL에 파라미터를 포함)
    $.ajax({
        url: `/member/adminUpdateKinderNo?username=${encodeURIComponent(username)}&kinderNo=${encodeURIComponent(kinderNo)}`,
        type: 'POST',
        success: function() {
            // 성공적으로 업데이트 후 알림 및 페이지 이동
            Swal.fire({
                title: "등록 완료",
                text: "창을 닫으면 메인 화면으로 돌아갑니다.",
                icon: "success",
                customClass: {
                    confirmButton: 'btn-ab btn-ab-swal'
                }
            }).then((result) => {
                window.location.href = '/main/admin'; // 성공 시 이동할 페이지
            });
        },
        error: function() {
            // 오류 처리
            Swal.fire({
                title: "오류 발생",
                text: "업데이트에 실패했습니다.",
                icon: "error"
            });

        }
    });
}