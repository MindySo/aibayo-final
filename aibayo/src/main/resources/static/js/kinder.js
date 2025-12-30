$(document).ready(function() {

    let url = '/kinder/list';
    commonAjax(url, 'GET');

    console.log('kinder.js');
    $('#selectkinder').on('change', function () {

        let kinderNo = $('#selectkinder').val();
        console.log(`kinderNo : ${kinderNo}`);

        let param = {
            kinderNo : kinderNo
        };
        let url ='/member/select';
        commonAjax(url, 'PUT', param);
    });


});

// afterSuccess 정의 및 호출
function afterSuccess(response, method) {
    console.log(`response : ${JSON.stringify(response)}`)
    if(method === 'GET'){
        for(let selectkinder of response ){
            console.log(`selectkinder : ${JSON.stringify(selectkinder)}`)
            // console.log(`selectkinder : ${selectkinder}`);
            let tag =
                `<option value="${selectkinder.kinderNo}">
                ${selectkinder.kinderName}
            </option>`;
            $('#selectkinder').append(tag);
        }
    }
    if(method === 'PUT'){
        console.log(`kinderNo 세팅 완료`);
        window.location.href="/main/admin";
    }




    console.log(`response:${JSON.stringify(response)}`)

}
