// JavaScript 코드
document.addEventListener('DOMContentLoaded', function() {
    // 모달 내에서 전송 버튼 클릭 시 실행될 함수
    document.getElementById('mediAddSubmit').addEventListener('click', function() {
        // 모달 내의 값들을 가져오기
        var selectType = document.querySelector('#exampleModal select.form-select').value;
        var selectFrequency = document.querySelectorAll('#exampleModal select.form-select')[1].value;
        var storageMethod = document.querySelector('#exampleModal input[name="flexRadioDefault"]:checked').nextElementSibling.textContent.trim();
        var dosage = document.getElementById('dose').value;
        var memo = document.getElementById('memo').value;
        var photo = document.querySelector('.pic .filename').textContent;

        // 추가할 내용을 만들기
        var newContent = `
          <div class="div-11">
            <div class="div-13">
              <div class="button">
                <button type="button" class="btn-close float-right" aria-label="Close" id="close"></button>
              </div>
              <table class="table">
                <tr>
                  <td colspan="2"><img src="medication/img/${photo}"></td>
                </tr>
                <tr>
                  <th>약의 종류</th>
                  <td>${selectType}</td>
                </tr>
                <tr>
                  <th>투약 횟수</th>
                  <td>${selectFrequency}</td>
                </tr>
                <tr>
                  <th>투약 시간</th>
                  <td>점심 식사 후</td> <!-- 예시로 고정된 값 -->
                </tr>
                <tr>
                  <th>보관 방법</th>
                  <td>${storageMethod}</td>
                </tr>
                <tr>
                  <th>특이 사항</th>
                  <td>${memo}</td>
                </tr>
              </table>
            </div>
          </div>
        `;

        // 새로운 내용을 추가할 위치 선택
        var frameWrapper8 = document.querySelector('.frame-wrapper-8');

        // frame-wrapper-8 안에 새로운 내용 추가
        frameWrapper8.insertAdjacentHTML('beforeend', newContent);

        // sign 테이블 처리
        var div11Count = document.querySelectorAll('.div-11').length;
        var signTable = document.querySelector('.sign');

        if (div11Count === 1) {
            // div-11이 한 개인 경우만 sign 테이블 추가
            signTable.style.display = 'block';
        } else {
            // div-11이 없거나 여러 개인 경우 sign 테이블 숨기기
            signTable.style.display = 'none';
        }

        // 모달 닫기
        var modal = document.getElementById('exampleModal');
        var modalInstance = bootstrap.Modal.getInstance(modal); // 부트스트랩 모달 객체 가져오기
        modalInstance.hide();
    });

    // x 클릭 시 메뉴명 input 삭제
    $(document).on('click', '.btn-close', function() {
        console.log("의뢰서 삭제");
        $(this).closest('.div-11').remove();

        // sign 테이블 처리
        var div11Count = document.querySelectorAll('.div-11').length;
        var signTable = document.querySelector('.sign');

        if (div11Count === 1) {
            // div-11이 한 개인 경우만 sign 테이블 추가
            signTable.style.display = 'block';
        } else {
            // div-11이 없거나 여러 개인 경우 sign 테이블 숨기기
            signTable.style.display = 'none';
        }
    });
});
