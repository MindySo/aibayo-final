function initMsg() {
    $('.msg').each(function () {
        $(this).hide();
    })
}

function setData(selector) {
    if (typeof setTitle === 'function') {
        setTitle(selector);
    }

    setBoardContent(selector);

    let isVisible = $('.liferecord').is(':visible') ? '1': '0';
    console.log(`isVisible: ${isVisible}`);
    selector.data('has-life-record', isVisible);

    selector.data('notepad-date', moment($('.datepicker').val()).format('YYYY-MM-DD'));
    // console.log(`datepicker: ${moment($('.datepicker').val()).format('YYYY-MM-DD')}`);
    // console.log(`notepad-date: ${selector.data('notepad-date')}`);

    if (isVisible === '1') {

        for (let i = 0; i < elementsToCheck.length; i++) {
            let element = '.' + elementsToCheck[i];
            // console.log(`element: ${element}`);

            if (isChecked($(element))) {
                // console.log(`클래스 선택자: ${element}`);
                // console.log(`체크된 값: ${$(element + ':checked').val()}`);
                selector.data(dataNames[i], $(element + ':checked').val());
            }
        }
    } else if (isVisible === '0') {
        for (const dataName of dataNames) {
            selector.data(dataName, '');
        }
    }
}

function isChecked(selector) {
    // console.log(`selector: ${selector}`);
    // console.log(`isChecked(${selector}) 실행`);
    initMsg();
    let bool = false;

    selector.each(function () {
        // console.log(`this: ${$(this)}`);
        if ($(this).is(':checked')) {
            // console.log('체크 있음');
            bool = true;
            return false;
        }

    });

    return bool;
}