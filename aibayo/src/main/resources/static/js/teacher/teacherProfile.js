document.addEventListener('DOMContentLoaded', () => {
    // 다른 초기화 함수 호출
    classListFilter();
    getCheckboxValue();
    updateLabelBackgroundColor();
    addLabelClickListeners();
    addInputClickListeners();
    addCheckboxEventListeners();
});

$(document).ready(function() {

    // 소속 반 수정 버튼 누를 때마다 초기화
    $(document).on('click', '#modifyClass', function () {
        classListFilter();
    });


    // 소속 반 수정하고 보여주기
    $(document).on('click', '#classModify', function () {

        let teacherId = $('.profile').attr('id');

        // 기존 반 불러오기
        let assignedElements = document.querySelectorAll('.assignedClassList');
        let assignedClassIds = Array.from(assignedElements).map(element => element.id);

        // 체크된 반 불러오기
        let checkedClassIds  = $('.form-check-input:checked').map(function() {
            return this.id;
        }).get();

        // 기존 반에는 있고 체크된 반에는 없음 : 삭제할 반
        let oldClassElements = Array.from(assignedElements).filter(element => !checkedClassIds.includes(element.id));
        let oldClassAcceptNos = oldClassElements.map(element => parseInt(element.getAttribute('data-accept-no')));

        // 기존 반에는 없고 체크된 반에는 있음 : 추가할 반
        // let newClassIds = Array.from(checkedClassIds).filter(id => !assignedClassIds.includes(id));
        let newClassIds = checkedClassIds.filter(id => !assignedClassIds.includes(id));

        let param = {
            oldClassAcceptNos: oldClassAcceptNos,
            newClassIds: newClassIds
        };

        console.log("assignedElements : " + assignedElements)
        console.log("assignedClassIds : " + assignedClassIds)
        console.log("checkedClassIds  : " + JSON.stringify(param));

        let url = "/teacher/teacherProfileAccept/" + teacherId;
        console.log(url);
        commonAjax(url, 'POST', param);
    });

    $('#resignTeacher').on('click', function () {
        Swal.fire({
            title: "퇴사를 승인하시겠습니까?",
            text: "승인 후 취소할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "승인",
            cancelButtonText: "취소"

        }).then((result) => {
            if (result.isConfirmed) {
                let url = "/teacher/deleteOk"

                let classes = document.querySelectorAll('.class');
                let classAcceptNos = Array.from(classes).map(element => parseInt(element.dataset.classAcceptNo));

                let param = {
                    id : parseInt($('.profile').attr('id')),
                    classAcceptNos : classAcceptNos,
                    kinderAcceptNo : $('.profile').data('kinder-accept-no')
                }
                console.log(`param: ${JSON.stringify(param)}`);

                commonAjax(url, 'DELETE', param);

            }
        });
    });

});

function afterSuccess(response, method) {
    console.log("method : " + method);
    console.log("response : " + response.kinderNo);

    if (method === 'DELETE' && response.kinderNo === null) {
        console.log("afterSuccess : DELETE");
        Swal.fire({
            title: "퇴사 승인 완료",
            text: "창을 닫으면 목록 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {

            window.location.href = window.location.origin + '/teacher/list';
        });
    }

    $('#assignedClasses').replaceWith($(response).find('#assignedClasses'));
    $('#staticBackdrop3').replaceWith($(response).find('#staticBackdrop3'));
    classListFilter();
    getCheckboxValue();
    updateLabelBackgroundColor();
    addLabelClickListeners();
    addInputClickListeners();
    addCheckboxEventListeners();
}



function classListFilter() {
    // 'classList' 클래스를 가진 모든 요소를 선택
    const finalClassElements = document.querySelectorAll('.classList');
    const addableElements = document.querySelectorAll('.addableClassList');
    const assignedElements = document.querySelectorAll('.assignedClassList');

    // 각 요소의 'id' 속성 값을 리스트로 반환
    const finalClass = Array.from(finalClassElements).map(element => element.id);
    const addableClass = Array.from(addableElements).map(element => element.id);
    const assignedClass = Array.from(assignedElements).map(element => element.id);

    // 결과를 콘솔에 출력
    console.log(finalClass);
    console.log(addableClass);
    console.log(assignedClass);

    // addableClass와 finalClass에서 동시에 존재하면서 assignedClass에는 없는 값 필터링
    const uniqueFinalClass = finalClass.filter(id => !addableClass.includes(id) && !assignedClass.includes(id));

    // <label class="classList"> 요소의 배경 색상 변경 및 <input> 비활성화
    finalClassElements.forEach(element => {
        if (uniqueFinalClass.includes(element.id)) {

            // 배경 색상 변경
            element.style.backgroundColor = '#ececec';

            // 하위 <div class="text-wrapper-3">의 색상 변경
            const textWrapper = element.querySelector('.text-wrapper-3');
            if (textWrapper) {
                textWrapper.style.color = 'gray';
            }

            // 체크박스 비활성화
            const checkbox = element.querySelector('input[type="checkbox"]');
            if (checkbox) {
                checkbox.disabled = true;
            }
        }
    });

    // <input type="checkbox" name="${class.className}"> 체크 상태로 설정
    assignedClass.forEach(id => {
        const checkbox = document.querySelector(`input[type="checkbox"][id="${id}"]`);
        if (checkbox) {
            checkbox.checked = true; // 체크 상태로 설정
        }
    });
}

function updateLabelBackgroundColor() {
    // 모든 체크박스를 선택
    const checkboxes = document.querySelectorAll('input.form-check-input[type="checkbox"]');

    checkboxes.forEach(checkbox => {
        // 체크박스의 체크 상태에 따라 배경 색상을 변경
        const label = checkbox.closest('label.class-selected.classList');
        if (label) {
            if (checkbox.checked) {
                label.style.backgroundColor = 'ivory'; // 배경 색상 변경
            }else if (checkbox.disabled) {
                label.style.backgroundColor = '#ececec'; // 배경 색상 변경
            } else {
                label.style.backgroundColor = ''; // 배경 색상 초기화 (기본값)
            }
        }
    });
}

function addCheckboxEventListeners() {
    // 모든 체크박스를 선택
    const checkboxes = document.querySelectorAll('input.form-check-input[type="checkbox"]');

    checkboxes.forEach(checkbox => {
        // 체크박스 상태가 변경될 때마다 updateLabelBackgroundColor 함수 호출
        checkbox.addEventListener('change', () => {
            updateLabelBackgroundColor();
        });
    });
}


function addLabelClickListeners() {
    // 모든 <label class="class-selected classList"> 요소 선택
    const labels = document.querySelectorAll('label.class-selected.classList');

    labels.forEach(label => {

        // 클릭 이벤트 리스너 추가
        label.addEventListener('click', (event) => {

            // 클릭한 label 내의 <input> 요소 선택
            const checkbox = label.querySelector('input.form-check-input');
            if (checkbox && !checkbox.disabled) {
                // <input> 요소의 checked 상태를 토글
                checkbox.checked = !checkbox.checked;
                // 상태 변경 시 관련된 CSS 업데이트
                updateLabelBackgroundColor();
                // 클릭 이벤트의 기본 동작을 막기 위해 이벤트 전파를 중지
                event.preventDefault();
            }
            checkLimit()
            getCheckboxValue();
        });
    });
}




function addInputClickListeners() {
    // 모든 <input class="form-check-input"> 요소 선택
    const inputs = document.querySelectorAll('input.form-check-input');

    inputs.forEach(input => {
        // 클릭 이벤트 리스너 추가
        input.addEventListener('click', () => {
            // 상태 변경 시 관련된 CSS 업데이트
            updateLabelBackgroundColor();
        });
    });
}

// 최대 선택 가능 체크박스 수
const MAX_CHECKBOX_SELECTION = 3;

function checkLimit() {
    // 현재 선택된 체크박스의 개수
    const selectedCheckboxes = document.querySelectorAll('input.form-check-input[type="checkbox"]:checked');

    if (selectedCheckboxes.length > MAX_CHECKBOX_SELECTION) {
        // 체크박스가 최대 선택 개수를 초과하면 경고 메시지 표시
        alert(`최대 ${MAX_CHECKBOX_SELECTION}개의 체크박스만 선택할 수 있습니다.`);

        // 선택한 체크박스 중 가장 마지막 체크를 해제
        const lastChecked = selectedCheckboxes[selectedCheckboxes.length-1];
        lastChecked.checked = false;
        updateLabelBackgroundColor();
    }
}

function getCheckboxValue()  {
    // 선택된 목록 가져오기
    const selectedList =
        document.querySelectorAll('input.form-check-input[type="checkbox"]:checked');

    // 선택된 목록에서 value 찾기
    let selectedCount = selectedList.length;

    // 출력
    document.getElementById('checkboxCount').innerText
        = `선택 가능한 반 ( ${3-selectedCount} / 3 ) 개`;

    console.log("getCheckboxValue : " + selectedCount);
}