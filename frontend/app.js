document.addEventListener('DOMContentLoaded', () => {
    const BASE_URL = 'http://localhost:8080'; // Update this if your backend is elsewhere

    // --- Section Switching Logic ---
    window.showSection = function(sectionId) {
        document.getElementById('zipFormSection').style.display = 'none';
        document.getElementById('infoSection').style.display = 'none';
        document.getElementById(sectionId).style.display = 'block';

        // Update active nav link
        document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('onclick').includes(sectionId)) {
                link.classList.add('active');
            }
        });
    }

    // --- Short URL Generation Logic ---
    const zipmeForm = document.getElementById('zipmeForm');
    const longUrlInput = document.getElementById('longUrlInput');
    const aliasInput = document.getElementById('aliasInput');
    const outputArea = document.getElementById('outputArea');
    const resultMessage = document.getElementById('resultMessage');
    const copyButton = document.getElementById('copyButton');
    const errorArea = document.getElementById('errorArea');
    const errorMessage = document.getElementById('errorMessage');

    zipmeForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const longUrl = longUrlInput.value.trim();
        const alias = aliasInput.value.trim();

        // Hide previous output/errors
        outputArea.style.display = 'none';
        errorArea.style.display = 'none';

        try {
            // Build the URL with parameters
            let url = `${BASE_URL}/zipme?longUrl=${encodeURIComponent(longUrl)}`;
            if (alias) {
                url += `&alias=${encodeURIComponent(alias)}`;
            }

            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            const textResult = await response.text();

            if (textResult.startsWith('Alias')) {
                // Backend returns "Alias X is already in use..." on failure
                errorMessage.textContent = textResult;
                errorArea.style.display = 'block';
            } else if (textResult.startsWith('Short Url:')) {
                // Backend returns "Short Url: http://localhost:8080/s/CODE" on success
                const shortUrl = textResult.replace('Short Url: ', '');
                resultMessage.innerHTML = `<strong>Success!</strong> Your short URL is: <a href="${shortUrl}" target="_blank">${shortUrl}</a>`;
                copyButton.setAttribute('data-url', shortUrl);
                outputArea.style.display = 'flex'; // Use flex to align elements in the alert
            } else {
                // Catch any unexpected success response
                 errorMessage.textContent = "An unknown error occurred while zipping the URL.";
                 errorArea.style.display = 'block';
            }

        } catch (error) {
            console.error('Fetch error:', error);
            errorMessage.textContent = 'Could not connect to the API. Ensure the backend is running.';
            errorArea.style.display = 'block';
        }
    });

    // Copy Button Logic
    copyButton.addEventListener('click', () => {
        const shortUrl = copyButton.getAttribute('data-url');
        navigator.clipboard.writeText(shortUrl).then(() => {
            copyButton.textContent = 'Copied!';
            setTimeout(() => {
                copyButton.textContent = 'Copy';
            }, 2000);
        }).catch(err => {
            console.error('Failed to copy: ', err);
            alert(`Failed to copy the URL. Please copy it manually: ${shortUrl}`);
        });
    });

    // In app.js, replace the existing infoForm listener with this:

infoForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const code = codeInput.value.trim();

    // Hide previous output/errors
    infoOutputArea.style.display = 'none';
    infoErrorArea.style.display = 'none';

    try {
        const response = await fetch(`${BASE_URL}/info/${encodeURIComponent(code)}`);
        
        // 1. Check HTTP Status
        if (!response.ok) {
            throw new Error(`Server returned status: ${response.status}`);
        }

        // 2. Read the entire response body as TEXT first
        const responseText = await response.text();

        // 3. Check for the specific "No data found" text response
        if (responseText.trim() === "No data found") {
            infoErrorMessage.textContent = `No URL mapping found for code: ${code}`;
            infoErrorArea.style.display = 'block';
            return; // Exit function after handling error
        }
        
        // 4. If it's not the "No data found" text, assume it's a valid JSON object and parse it
        const data = JSON.parse(responseText);

        // SUCCESS: Populate the details
        document.getElementById('infoCodeDisplay').textContent = data.code;
        document.getElementById('infoLongUrl').textContent = data.longUrl;
        document.getElementById('infoShortUrl').textContent = `${BASE_URL}/s/${data.shortCode}`;
        document.getElementById('infoClicks').textContent = data.clickCount;


        infoOutputArea.style.display = 'block';

    } catch (error) {
        console.error('Fetch error:', error);
        // This catch block now handles actual network errors or JSON parsing failures.
        infoErrorMessage.textContent = `Could not fetch info. Error: ${error.message || 'Check network connection.'}`;
        infoErrorArea.style.display = 'block';
    }
});
});