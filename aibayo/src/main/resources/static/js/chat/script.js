const chatInput = document.querySelector(".chat-input textarea")
const sendChatBtn = document.querySelector(".chat-input span")
const chatbox = document.querySelector(".chatbox")
const chatbotToggler = document.querySelector(".chatbot-toggler")


let userMessage;
const inputInitHeight = chatInput.scrollHeight;



const createChatLi = (message, className) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", className);
    let chatContent =  className === "outgoing" ? '<p></p>' : '<span class="material-symbols-outlined">smart_toy</span><p></p>';
    chatLi.innerHTML = chatContent;
    chatLi.querySelector("p").textContent = message;
    return chatLi;
}

let API_KEY = "";

// 페이지 로드 시 API 키를 가져옴
document.addEventListener("DOMContentLoaded", () => {
    fetchApiKey();
});

// API 키를 서버에서 가져옴
const fetchApiKey = async () => {
    try {
        const response = await fetch('/api-key');
        if (response.ok) {
            API_KEY = await response.text();
        } else {
            console.error('Failed to fetch API key');
        }
    } catch (error) {
        console.error('Error fetching API key:', error);
    }
};


const generateResponse = (incomingChatLi) => {
    const API_URL = "https://api.openai.com/v1/chat/completions";
    const messageElement = incomingChatLi.querySelector("p");
    const requestOptions = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${API_KEY}`
        },
        body: JSON.stringify({
            model: "gpt-4o-mini",
            messages: [{ role: "user", content: userMessage }]
        })
    }
    fetch(API_URL, requestOptions)
        .then(response => response.json())
        .then(data => {
            messageElement.textContent = data.choices[0].message.content;
        })
        .catch((error) => {
            messageElement.textContent = "다시 시도해주세요";
        });
};

const handleChat = () => {
    userMessage = chatInput.value.trim();
    if (!userMessage) return;
    chatInput.style.height = `${inputInitHeight}px`; // 채팅 보내면 채팅 입력창 높이 초기화
    // 작성한 채팅 생성
    chatbox.appendChild(createChatLi(userMessage, "outgoing"));
    // 채팅치면 textarea 태그에 있는 작성글 사라지게
    chatInput.value = "";
    // 채팅창 스크롤
    chatbox.scrollTo(0, chatbox.scrollHeight);

    setTimeout(() => {
        // display "생각중.." message while waiting for the response
        const incomingChatLi= createChatLi("답변 중...", "incoming") ;
        chatbox.appendChild(incomingChatLi);
        generateResponse(incomingChatLi);
    }, 600);
}

chatInput.addEventListener("input", () => {
    // 채팅 입력창 크기 조절
    chatInput.style.height = `${inputInitHeight}px`;
    chatInput.style.height = `${chatInput.scrollHeight}px`;
});

chatInput.addEventListener("keydown", (e) => {
    // enter 키 누르면 전송, shift+enter 키 누르면 줄 바꿈
    if (e.key === "Enter" && !e.shiftKey) {
        e.preventDefault();
        handleChat();
    }
});

sendChatBtn.addEventListener("click", handleChat);
chatbotToggler.addEventListener("click", () => {
    document.body.classList.toggle("show-chatbot");
    if (document.body.classList.contains("show-chatbot")) {
        chatIframe.style.width = '460px';  // 원하는 크기로 설정
        chatIframe.style.height = '680px'; // 원하는 크기로 설정
    } else {
        chatIframe.style.width = '50px';
        chatIframe.style.height = '50px';
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const chatbotToggler = document.querySelector(".chatbot-toggler");

    chatbotToggler.addEventListener("click", () => {
        parent.document.body.classList.toggle("show-chatbot");
        const chatIframe = parent.document.getElementById("chatIframe");
        if (parent.document.body.classList.contains("show-chatbot")) {
            chatIframe.style.width = '460px';  // 원하는 크기로 설정
            chatIframe.style.height = '680px'; // 원하는 크기로 설정
        } else {
            chatIframe.style.width = '100px';
            chatIframe.style.height = '100px';
        }
    });
});

// 페이지 로드 시 채팅팝업을 끈 상태로 설정
document.addEventListener("DOMContentLoaded", () => {
    document.body.classList.remove("show-chatbot");
    // chatbot.style.width = '50px';
    // chatbot.style.height = '50px';
});