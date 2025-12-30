document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM fully loaded and parsed');

    // Event listener for clicking on class items
    $('.class-item').on('click', function () {
        console.log('Class item clicked');
        const classNo = $(this).data('classno');
        console.log("classNo: " + classNo);

        // Fetch data using classNo from the backend (replace with actual data retrieval logic)
        const classData = getClassByNo(classNo);

        if (classData) {
            // $('#classModalLabel').text(classData.className + ' 정보');
            $('#className').text(classData.className);

            // Populate teacher list
            const teacherList = $('#teacherList');
            teacherList.empty();
            if (classData.teachers && Array.isArray(classData.teachers)) {
                classData.teachers.forEach(teacher => {
                    teacherList.append(`<li class="list-group-item">${teacher}</li>`);
                });
            }

            // Populate students list
            const kidList = $('#kidList');
            kidList.empty();
            if (classData.students && Array.isArray(classData.students)) {
                classData.students.forEach(student => {
                    kidList.append(`<li class="list-group-item">${student}</li>`);
                });
            }

            // Set notice link
            if (classData.noticeLink) {
                $('#noticeLink').attr('href', classData.noticeLink);
            }

            // Show the modal
            $('#classModal').modal('show');
        } else {
            Swal.fire('데이터 없음', '해당 반의 데이터를 찾을 수 없습니다.', 'error');
        }
    });


    $('.delete-btn').on('click', function (event) {
        event.stopPropagation();  // Prevent triggering the click event on the class-item
        console.log('Delete button clicked');

        // 'delete-btn' 버튼이 속한 'class-item' 요소에서 'hidden' input 값을 가져오기
        const classItem = $(this).closest('.class-item'); // 해당 class-item 요소를 찾음
        const classNo = classItem.find('.hidden-class-no').val(); // hidden input에서 classNo 값을 가져옴
        console.log('Class No to delete:', classNo); // classNo 값을 콘솔에 출력
        if (!classNo) {
            console.error("classNo is not defined.");
            return; // classNo가 없으면 더 이상 진행하지 않음
        }


        // 확인 메시지를 띄워 사용자에게 삭제를 확인받음
        Swal.fire({
            title: '정말로 삭제하시겠습니까?',
            text: "이 작업은 되돌릴 수 없습니다!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: '네, 삭제합니다!',
            cancelButtonText: '취소'
        }).then((result) => {
            if (result.isConfirmed) {
                // AJAX 요청을 통해 서버에 삭제 요청을 보냄
                $.ajax({
                    url: '/classManage/deleteClass', // 컨트롤러의 URL
                    method: 'POST', // POST 메서드 사용 (삭제 요청이므로 안전하게 POST 사용)
                    contentType: 'application/json',
                    data: JSON.stringify({ classNo: classNo }), // classNo 데이터를 JSON 형식으로 보냄
                    success: function (response) {
                        Swal.fire('삭제되었습니다!', '', 'success').then(() => {
                            classItem.remove(); // 삭제가 성공하면 해당 항목을 UI에서 제거
                            $('#classModal').modal('hide'); // 모달을 닫음
                        });
                    },
                    error: function (xhr, status, error) {
                        console.log('Error:', error);
                        Swal.fire('오류가 발생했습니다.', xhr.responseText, 'error');
                    }
                });
            }
        });
    });

    // Event listener for editing the class name
    // Bootstrap 모달이 기본적으로 포커스를 강제하는 기능을 가지고 있어서 sweetalert2과 충돌남
    $.fn.modal.Constructor.prototype._enforceFocus = function () {
    }; // Bootstrap 모달이 포커스를 강제하지 않도록 설정
    new bootstrap.Modal(document.getElementById('classModal'), {focus: false}); // Bootstrap 모달이 로드될 때 자동으로 포커스를 받지 않도록 설정

    $('#editClassNameBtn').on('click', async function () {
        const currentClassName = $('#className').text(); // 현재 반 이름을 가져옵니다.
        const classNo = $(this).data('classNo'); // 버튼의 data-classNo 속성에서 classNo를 가져옵니다
        console.log('Class No:', classNo); // 가져온 classNo를 로그로 출력

        Swal.fire({
            title: '새 반 이름을 입력하세요',
            input: 'text',
            inputValue: currentClassName,
            showCancelButton: true,
            confirmButtonText: '저장',
            cancelButtonText: '취소',
            inputValidator: (value) => {
                if (!value) {
                    return '반 이름을 입력해야 합니다!';
                }
            }
        }).then((result) => {
            if (result.isConfirmed) {
                const newClassName = result.value;
                $('#className').text(newClassName);

                // AJAX 요청을 통해 서버에 새로운 반 이름을 전달
                $.ajax({
                    url: '/classManage/updateClassName', // 컨트롤러의 URL
                    method: 'POST', // POST 메서드 사용
                    contentType: 'application/json',
                    data: JSON.stringify({
                        classNo: classNo, // classNo를 보냅니다
                        newClassName: newClassName // 새 반 이름을 보냅니다
                    }),
                    success: function (response) {
                        window.location.href = '/classManage/main';

                    },
                    error: function (xhr, status, error) {
                        console.log('Error:', error);
                        Swal.fire('오류 발생.', xhr.responseText, 'error');
                    }
                });
            }
        });
    });

    $('#createClassBtn').on('click', function () {
        Swal.fire({
            title: '새 반 이름을 입력하세요',
            input: 'text',
            inputPlaceholder: '반 이름',
            showCancelButton: true,
            confirmButtonText: '저장',
            cancelButtonText: '취소',
            inputValidator: (value) => {
                if (!value) {
                    return '반 이름을 입력해야 합니다!';
                }
            }
        }).then((result) => {
            if (result.isConfirmed) {
                const newClassName = result.value;

                // AJAX 요청을 통해 서버에 새로운 반 이름을 전달
                $.ajax({
                    url: '/classManage/createClass', // 컨트롤러의 URL
                    method: 'POST', // POST 메서드 사용
                    contentType: 'application/json',
                    data: JSON.stringify({ className: newClassName }), // 새 반 이름을 JSON 형식으로 보냄
                    success: function(response) {
                        Swal.fire('반이 성공적으로 추가되었습니다!', '', 'success').then(() => {
                            window.location.reload(); // 성공적으로 추가되면 페이지를 새로고침하여 반 목록을 업데이트
                        });
                    },
                    error: function (xhr, status, error) {
                        console.log('Error:', error);
                        Swal.fire('오류 발생.', xhr.responseText, 'error');
                    }
                });
            }
        });
    });

    function getClassByNo(classNo) {
        return $.ajax({
            url: `/classManage/detail/${classNo}`,
            method: 'GET',
            contentType: "application/json",
            success: function (data) {
                console.log("Class Data:", data);
                // data.classKid와 data.classTeacher를 사용하여 모달을 업데이트
                // $('#classModalLabel').text(data.className + ' 정보');
                // $('#classModalLabel').text('반 정보');
                // $('#className').text(data.className);
                // data.classTeacher에서 className을 가져와서 설정
                if (data.classTeacher.length > 0) {
                    $('#className').text(data.classTeacher[0].className);
                    $('#editClassNameBtn').data('classNo', classNo);
                    $('.delete-btn').data('classNo', classNo);
                } else {
                    $('#className').text('No class name available');
                }

                // 교사 목록 업데이트
                const teacherList = $('#teacherList');
                teacherList.empty();
                data.classTeacher.forEach(teacher => {
                    teacherList.append(`<li class="list-group-item">${teacher.name}</li>`);
                });

                // 학생 목록 업데이트
                const kidList = $('#kidList');
                kidList.empty();
                data.classKid.forEach(kid => {
                    kidList.append(`<li class="list-group-item">${kid.kidName}</li>`);
                });

                // 모달 표시
                $('#classModal').modal('show');
            },
            error: function (error) {
                console.log("Error:", error);
                Swal.fire('오류', '반 정보를 가져오는데 문제가 발생했습니다.', 'error');
            }

        })
    }

});
