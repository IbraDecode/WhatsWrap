javascript:(function() {
    var observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.addedNodes.length > 0) {
                // Look for elements that indicate a new message
                // This is a simplified example, actual detection might be more complex
                var newMessageIndicator = document.querySelector("._2Ts6i"); // Example: badge for unread messages
                if (newMessageIndicator && newMessageIndicator.innerText !== "") {
                    // Send a notification to the Android app
                    Android.showNotification("Pesan Baru", "Anda memiliki pesan baru!");
                }
            }
        });
    });

    // Start observing the document body for changes
    observer.observe(document.body, { childList: true, subtree: true });
})();

