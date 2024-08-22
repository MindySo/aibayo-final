$(document).ready(function () {
    // 시각적으로 비활성화된 스타일 적용
    $('.none-clickable').css({
        'pointer-events': 'none'
    });

    // 초기 로딩 시 너비 설정
    setMealItemEqualWidth();

    // 윈도우 리사이즈 시에도 재설정
    $(window).resize(function () {
        setMealItemEqualWidth();
    });

    setWeather();
    setDust();

    $(document).on('click', '.select_kid', function () {
        let url = "/main/user/changeKid";
        let param = {
            kidNo: $(this).data('kid-no')
        }
        console.log(`param: ${JSON.stringify(param)}`);

        commonAjax(url, 'POST', param);
    });
});

function afterSuccess(response) {
    // console.log(`response: ${response}`);

    $('.main_top_box').replaceWith($(response).find('.main_top_box'));
    $('.recent_board_box').replaceWith($(response).find('.recent_board_box'));
    $('.weather_dust_box').replaceWith($(response).find('.weather_dust_box'));

    // 시각적으로 비활성화된 스타일 적용
    $('.none-clickable').css({
        'pointer-events': 'none'
    });

    // 초기 로딩 시 너비 설정
    setMealItemEqualWidth();

    // 윈도우 리사이즈 시에도 재설정
    $(window).resize(function () {
        setMealItemEqualWidth();
    });

    setWeather();
    setDust();
}

function setMealItemEqualWidth() {
    let meals = $('.meal_item');
    if (meals.length > 3) {
        let mealWidth = $('.meal_item:first-child').outerWidth();
        // console.log(`mealWidth: ${mealWidth}`);

        meals.each(function () {
            $(this).css({
                width : mealWidth + 'px',
                flex : '0'
            });
        });
    }
}

function setWeather() {
    const ptyDesc = ["없음", "비", "비/눈", "눈", "", "빗방울", "빗방울눈날림", "눈날림"];
    const ptySvg = [
        ``,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 48 48"><path fill="#8ac2ff" d="M23.999 6c6.337 0 9.932 4.195 10.455 9.26h.16c4.078 0 7.384 3.298 7.384 7.365s-3.306 7.365-7.384 7.365l-1.723.001l-.004.009h-.586l-1.971 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13l-4.259.001l-.003.009h-.85l-1.971 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13l-3.995.001l-.003.009h-1.113l-1.972 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13h-.029C9.306 29.99 6 26.693 6 22.625s3.306-7.365 7.384-7.365h.16C14.07 10.161 17.662 6 24 6m-6.37 28.67a1.25 1.25 0 0 1 .45 1.71l-1.75 3a1.25 1.25 0 1 1-2.159-1.26l1.75-3a1.25 1.25 0 0 1 1.71-.45m8.45 1.71a1.25 1.25 0 1 0-2.159-1.26l-1.75 3a1.25 1.25 0 1 0 2.16 1.26z"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#8ac2ff" d="M14.534 17.553a.75.75 0 1 1 0 1.5a.75.75 0 0 1 0-1.5M12 4.001c3.169 0 4.966 2.097 5.227 4.63h.08A3.687 3.687 0 0 1 21 12.314a3.687 3.687 0 0 1-3.692 3.682L16 15.997a.75.75 0 0 1-1.433 0l-5.58-.001l-1.582 2.629a.75.75 0 0 1-1.344-.659l.045-.091l1.15-1.879h-.563A3.687 3.687 0 0 1 3 12.314A3.687 3.687 0 0 1 6.693 8.63h.08C7.035 6.08 8.831 4 12 4m-.422 12.704a.75.75 0 0 1 .32.933l-.046.091l-.556.896a.75.75 0 0 1-1.345-.658l.045-.092l.557-.896a.75.75 0 0 1 1.025-.274m5.455.098a.75.75 0 1 1 0 1.5a.75.75 0 0 1 0-1.5"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 48 48"><path fill="#8ac2ff" d="M19.502 36a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m-13.5-2a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m-9-26c6.338 0 9.933 4.195 10.456 9.26h.16c4.078 0 7.384 3.298 7.384 7.365s-3.306 7.365-7.385 7.365H13.389c-4.078 0-7.384-3.297-7.384-7.365s3.306-7.365 7.384-7.365h.16C14.074 12.161 17.666 8 24.003 8"/></svg>`,
        ``,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 48 48"><path fill="#8ac2ff" d="M23.999 6c6.337 0 9.932 4.195 10.455 9.26h.16c4.078 0 7.384 3.298 7.384 7.365s-3.306 7.365-7.384 7.365l-1.723.001l-.004.009h-.586l-1.971 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13l-4.259.001l-.003.009h-.85l-1.971 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13l-3.995.001l-.003.009h-1.113l-1.972 3.38a1.25 1.25 0 1 1-2.16-1.26l1.243-2.13h-.029C9.306 29.99 6 26.693 6 22.625s3.306-7.365 7.384-7.365h.16C14.07 10.161 17.662 6 24 6m-6.37 28.67a1.25 1.25 0 0 1 .45 1.71l-1.75 3a1.25 1.25 0 1 1-2.159-1.26l1.75-3a1.25 1.25 0 0 1 1.71-.45m8.45 1.71a1.25 1.25 0 1 0-2.159-1.26l-1.75 3a1.25 1.25 0 1 0 2.16 1.26z"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#8ac2ff" d="M14.534 17.553a.75.75 0 1 1 0 1.5a.75.75 0 0 1 0-1.5M12 4.001c3.169 0 4.966 2.097 5.227 4.63h.08A3.687 3.687 0 0 1 21 12.314a3.687 3.687 0 0 1-3.692 3.682L16 15.997a.75.75 0 0 1-1.433 0l-5.58-.001l-1.582 2.629a.75.75 0 0 1-1.344-.659l.045-.091l1.15-1.879h-.563A3.687 3.687 0 0 1 3 12.314A3.687 3.687 0 0 1 6.693 8.63h.08C7.035 6.08 8.831 4 12 4m-.422 12.704a.75.75 0 0 1 .32.933l-.046.091l-.556.896a.75.75 0 0 1-1.345-.658l.045-.092l.557-.896a.75.75 0 0 1 1.025-.274m5.455.098a.75.75 0 1 1 0 1.5a.75.75 0 0 1 0-1.5"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 48 48"><path fill="#8ac2ff" d="M19.502 36a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m-13.5-2a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m9 0a1.5 1.5 0 1 1 0 3a1.5 1.5 0 0 1 0-3m-9-26c6.338 0 9.933 4.195 10.456 9.26h.16c4.078 0 7.384 3.298 7.384 7.365s-3.306 7.365-7.385 7.365H13.389c-4.078 0-7.384-3.297-7.384-7.365s3.306-7.365 7.384-7.365h.16C14.074 12.161 17.666 8 24.003 8"/></svg>`
    ]
    const skyDesc = ["", "맑음", "", "구름많음", "흐림"];
    const skySvg = [
        ``,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#FFB505" d="M12 2a.75.75 0 0 1 .75.75v1.5a.75.75 0 0 1-1.5 0v-1.5A.75.75 0 0 1 12 2m5 10a5 5 0 1 1-10 0a5 5 0 0 1 10 0m4.25.75a.75.75 0 0 0 0-1.5h-1.5a.75.75 0 0 0 0 1.5zM12 19a.75.75 0 0 1 .75.75v1.5a.75.75 0 0 1-1.5 0v-1.5A.75.75 0 0 1 12 19m-7.75-6.25a.75.75 0 0 0 0-1.5h-1.5a.75.75 0 0 0 0 1.5zm-.03-8.53a.75.75 0 0 1 1.06 0l1.5 1.5a.75.75 0 0 1-1.06 1.06l-1.5-1.5a.75.75 0 0 1 0-1.06m1.06 15.56a.75.75 0 1 1-1.06-1.06l1.5-1.5a.75.75 0 1 1 1.06 1.06zm14.5-15.56a.75.75 0 0 0-1.06 0l-1.5 1.5a.75.75 0 0 0 1.06 1.06l1.5-1.5a.75.75 0 0 0 0-1.06m-1.06 15.56a.75.75 0 1 0 1.06-1.06l-1.5-1.5a.75.75 0 1 0-1.06 1.06z"/></svg>`,
        ``,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 512 512"><path fill="#bababa" d="M376 432H116c-32.37 0-60.23-8.57-80.59-24.77C12.24 388.78 0 361.39 0 328c0-57.57 42-90.58 87.56-100.75a16 16 0 0 0 12.12-12.39c7.68-36.68 24.45-68.15 49.18-92A153.57 153.57 0 0 1 256 80c35.5 0 68.24 11.69 94.68 33.8a156.24 156.24 0 0 1 42.05 56a16 16 0 0 0 11.37 9.16c27 5.61 51.07 17.33 69.18 33.85C498.61 235.88 512 267.42 512 304c0 36-14.38 68.88-40.49 92.59C446.36 419.43 412.44 432 376 432"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 24 24"><path fill="#bababa" d="M13.002 7.009c3.168 0 4.966 2.097 5.227 4.63h.08a3.687 3.687 0 0 1 3.692 3.683a3.687 3.687 0 0 1-3.692 3.682H7.694a3.687 3.687 0 0 1-3.692-3.682a3.687 3.687 0 0 1 3.692-3.683h.08c.263-2.55 2.06-4.63 5.228-4.63M10 4c1.617 0 3.05.815 3.9 2.062a8 8 0 0 0-.898-.053c-2.994 0-5.171 1.677-5.937 4.213l-.068.24l-.058.238l-.206.039a4.68 4.68 0 0 0-3.449 3.045a3.282 3.282 0 0 1 1.812-5.881l.257-.006A4.72 4.72 0 0 1 10 4"/></svg>`
    ];

    let now = moment();
    let base_date = moment(now).format('YYYYMMDD');
    // console.log(`base_date: ${base_date}`);
    let base_time = moment(now).format('HH30');
    // console.log(`base_time: ${base_time}`);
    let nowMinute = moment(now).format('mm');
    // console.log(`nowMinute: ${nowMinute}`)
    const checkPoint = 45;

    if (nowMinute < checkPoint) {
        // 자정일 경우
        base_date = moment(now).subtract('1', 'H').format('YYYYMMDD');
        base_time = moment(now).subtract('1', 'H').format('HH30');
    }
    // console.log(`base_date final: ${base_date}`);
    // console.log(`base_time final: ${base_time}`);

    let nx = $('.weather_box').data('nx');
    let ny = $('.weather_box').data('ny');

    let url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";
    let queryParams = `?${encodeURIComponent('serviceKey')}=5zv6%2FkeqLoX546ceoeLoe%2BdLSxW9ljqCM8YBFhS9sEDtq%2FqUlFvw0jMsYmaQu5qeim81NC%2FEg0f%2Fxgk%2FATVI9A%3D%3D`;
    queryParams += `&${encodeURIComponent('pageNo')}=${encodeURIComponent('1')}`;
    queryParams += `&${encodeURIComponent('numOfRows')}=${encodeURIComponent('60')}`;
    queryParams += `&${encodeURIComponent('dataType')}=${encodeURIComponent('JSON')}`;
    queryParams += `&${encodeURIComponent('base_date')}=${encodeURIComponent(base_date)}`;
    queryParams += `&${encodeURIComponent('base_time')}=${encodeURIComponent(base_time)}`;
    queryParams += `&${encodeURIComponent('nx')}=${encodeURIComponent(nx)}`;
    queryParams += `&${encodeURIComponent('ny')}=${encodeURIComponent(ny)}`;

    $.ajax({
        type: 'GET',
        url: url + queryParams,
        success: function (response) {
            // console.log(`response: ${JSON.stringify(response)}`);
            const pty = 6;
            const sky = 18;
            const t1h = 24;

            let weatherInfo = response.response.body.items.item;
            let ptyInfo = weatherInfo[pty].fcstValue;
            let skyInfo = weatherInfo[sky].fcstValue;
            let t1hInfo = weatherInfo[t1h].fcstValue;
            // console.log(`ptyInfo: ${ptyInfo}`);
            // console.log(`skyInfo: ${skyInfo}`);
            // console.log(`t1hInfo: ${t1hInfo}`);


            let tag = `<div class="weather_box" data-nx="${nx}" data-ny="${ny}">

                                <div class="weather_text_box">
                    
                                    <div class="weather_title">날씨 정보</div>
                    
                                    <div class="weather_body">
                                        <div class="weather_temperature">${t1hInfo}℃</div>
                                        `;

            if (ptyInfo == 0) {
                                tag += `<div class="weather_desc">${skyDesc[skyInfo]}</div>
                                                    </div>
                                    
                                                </div>
                                    
                                                <div class="weather_svg">
                                                    ${skySvg[skyInfo]}
                                                </div>
                                    
                                            </div>`;
            }

            if (ptyInfo > 0) {
                                tag += `<div class="weather_desc">${ptyDesc[ptyInfo]}</div>
                                                                    </div>
                                                    
                                                                </div>
                                                    
                                                                <div class="weather_svg">
                                                                    ${ptySvg[ptyInfo]}
                                                                </div>
                                                    
                                                            </div>`;
            }


            $('.weather_box').replaceWith(tag);
        },
        error: function (error) {
            console.log(`error: ${error}`);
        }
    });
}

function setDust() {
    const pmDesc = ["", "좋음", "보통", "나쁨", "매우 나쁨"];
    const pmSvg = [
        "",
        `<svg xmlns="http://www.w3.org/2000/svg" width="90" height="90" viewBox="0 0 24 24"><path fill="#3C83EF" d="M12 17.5c2.33 0 4.3-1.46 5.11-3.5H6.89c.8 2.04 2.78 3.5 5.11 3.5M8.5 11A1.5 1.5 0 0 0 10 9.5A1.5 1.5 0 0 0 8.5 8A1.5 1.5 0 0 0 7 9.5A1.5 1.5 0 0 0 8.5 11m7 0A1.5 1.5 0 0 0 17 9.5A1.5 1.5 0 0 0 15.5 8A1.5 1.5 0 0 0 14 9.5a1.5 1.5 0 0 0 1.5 1.5M12 20a8 8 0 0 1-8-8a8 8 0 0 1 8-8a8 8 0 0 1 8 8a8 8 0 0 1-8 8m0-18C6.47 2 2 6.5 2 12a10 10 0 0 0 10 10a10 10 0 0 0 10-10A10 10 0 0 0 12 2"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="90" height="90" viewBox="0 0 256 256"><path fill="#048A24" d="M178.39 158c-11 19.06-29.39 30-50.39 30s-39.36-10.93-50.39-30a12 12 0 0 1 20.78-12c3.89 6.73 12.91 18 29.61 18s25.72-11.28 29.61-18a12 12 0 1 1 20.78 12M236 128A108 108 0 1 1 128 20a108.12 108.12 0 0 1 108 108m-24 0a84 84 0 1 0-84 84a84.09 84.09 0 0 0 84-84m-120-4a16 16 0 1 0-16-16a16 16 0 0 0 16 16m72-32a16 16 0 1 0 16 16a16 16 0 0 0-16-16"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="90" height="90" viewBox="0 0 256 256"><path fill="#FFB505" d="M128 20a108 108 0 1 0 108 108A108.12 108.12 0 0 0 128 20m0 192a84 84 0 1 1 84-84a84.09 84.09 0 0 1-84 84m52-52a12 12 0 0 1-12 12H88a12 12 0 0 1 0-24h80a12 12 0 0 1 12 12M76 108a16 16 0 1 1 16 16a16 16 0 0 1-16-16m104 0a16 16 0 1 1-16-16a16 16 0 0 1 16 16"/></svg>`,
        `<svg xmlns="http://www.w3.org/2000/svg" width="90" height="90" viewBox="0 0 256 256"><path fill="#EC1C1C" d="M128 20a108 108 0 1 0 108 108A108.12 108.12 0 0 0 128 20m0 192a84 84 0 1 1 84-84a84.09 84.09 0 0 1-84 84M76 108a16 16 0 1 1 16 16a16 16 0 0 1-16-16m104 0a16 16 0 1 1-16-16a16 16 0 0 1 16 16m-3.26 57a12 12 0 0 1-19.48 14a36 36 0 0 0-58.52 0a12 12 0 0 1-19.48-14a60 60 0 0 1 97.48 0"/></svg>`
    ]

    let stationName = $('.dust_box').data('station-name');
    // console.log(`stationName: ${stationName}`);

    let url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";
    let queryParams = `?${encodeURIComponent('serviceKey')}=5zv6%2FkeqLoX546ceoeLoe%2BdLSxW9ljqCM8YBFhS9sEDtq%2FqUlFvw0jMsYmaQu5qeim81NC%2FEg0f%2Fxgk%2FATVI9A%3D%3D`;
    queryParams += `&${encodeURIComponent('returnType')}=${encodeURIComponent('json')}`;
    queryParams += `&${encodeURIComponent('numOfRows')}=${encodeURIComponent('1')}`;
    queryParams += `&${encodeURIComponent('stationName')}=${encodeURIComponent(stationName)}`;
    queryParams += `&${encodeURIComponent('dataTerm')}=${encodeURIComponent('DAILY')}`;
    queryParams += `&${encodeURIComponent('ver')}=${encodeURIComponent('1.3')}`;

    $.ajax({
        type: 'GET',
        url: url + queryParams,
        success: function (response) {
            let item = response.response.body.items[0];
            let pm10Grade1h = item.pm10Grade1h;
            let pm25Grade1h = item.pm25Grade1h == null ? 1 : item.pm25Grade1h;
            // console.log(`item: ${JSON.stringify(response.response.body.items[0])}`);
            // console.log(`pm10Grade1h: ${pm10Grade1h}`)
            // console.log(`pm25Grade1h: ${pm25Grade1h}`)

            let tag = `<div class="dust_box" data-station-name="${stationName}">

                                <div class="fine_box">
                                    <div class="fine_body">
                                        <div class="fine_title">미세먼지</div>
                                        <div class="pm_svg_box">
                                            ${pmSvg[pm10Grade1h]}
                                        </div>
                                        <div class="fine_desc pm_grade_${pm10Grade1h}">
                                            ${pmDesc[pm10Grade1h]}
                                        </div>
                                    </div>
                                </div>
                    
                                <div class="fine_box">
                                    <div class="fine_body">
                                        <div class="fine_title">초미세먼지</div>
                                        <div class="pm_svg_box">
                                            ${pmSvg[pm25Grade1h]}
                                        </div>
                                        <div class="fine_desc pm_grade_${pm25Grade1h}">
                                            ${pmDesc[pm25Grade1h]}
                                        </div>
                                    </div>
                                </div>
                    
                            </div>`;

            $('.dust_box').replaceWith(tag);
        },
        error: function (error) {
            console.log(`error: ${error}`);
        }
    });
}