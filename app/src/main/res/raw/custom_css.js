javascript:(function() {
    var style = document.createElement('style');
    style.innerHTML = `
        #side { display: none !important; }
        #main { width: 100% !important; left: 0 !important; }
        header[data-testid='conversation-header'] {
            height: 56px !important;
            background-color: #075E54 !important;
            color: white !important;
        }
        #main .copyable-area {
            background-color: #ECE5DD !important;
            background-image: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzQwIiBoZWlnaHQ9IjM0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGRlZnM+CjxwYXR0ZXJuIGlkPSJhIiBwYXR0ZXJuVW5pdHM9InVzZXJTcGFjZU9uVXNlIiB3aWR0aD0iMzQwIiBoZWlnaHQ9IjM0MCI+CjxwYXRoIGZpbGw9IiNmMGYwZjAiIGQ9Im0wIDBoMzQwdjM0MGgtMzQweiIvPgo8L3BhdHRlcm4+CjwvZGVmcz4KPHJlY3Qgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgZmlsbD0idXJsKCNhKSIvPgo8L3N2Zz4=') !important;
        }
        .message-out .copyable-text {
            background-color: #DCF8C6 !important;
            border-radius: 16px !important;
            padding: 8px 12px !important;
            margin: 4px !important;
        }
        .message-in .copyable-text {
            background-color: white !important;
            border-radius: 16px !important;
            padding: 8px 12px !important;
            margin: 4px !important;
        }
        footer {
            height: 48px !important;
            background-color: white !important;
            position: sticky !important;
            bottom: 0 !important;
        }
        ._2Ts6i { display: none !important; }
        ._3j7s9 { display: none !important; }
    `;
    document.head.appendChild(style);
})()

