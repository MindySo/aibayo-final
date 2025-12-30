function afterSuccess(response) {
    $('.class_90').replaceWith($(response).find('.class_90'));
    $('.pagination').replaceWith($(response).find('.pagination'));
}