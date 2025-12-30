function findByDate(parameter) {
    console.log('dateText: ' + parameter.dateText);

    let url = '/notepad/user/searchDate';
    let data = {
        kidNo : parameter.kidNo,
        notepadDate: parameter.dateText
    }

    commonAjax(url, 'POST', data);

}
