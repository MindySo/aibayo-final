$(document).ready(function () {
    // 현재 로컬 시간을 가져옴
    const currentTime = new Date();
    console.log('currentTime : ' + currentTime);

    // 현재 시간을 'yyyy-MM-ddTHH:mm' 형식으로 변환하는 함수
    function formatDateToRunDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day}T${hours}:${minutes}`;
    }

    // 변환된 현재 시간
    const nowTime = formatDateToRunDate(currentTime);
    console.log('nowTime : ' + nowTime);

    // 예시로 runDate 값을 가져오고 출력
    const runDate = $('#runDate').val();
    console.log('runDate : ' + runDate);
});
