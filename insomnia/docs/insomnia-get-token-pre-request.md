# Pre-request script para pegar token jwt
- Usa-se um fetch e variáveis de ambiente para pegar o access_token
- No geral é mais fácil usar a tag no Header configurando o outro request na collection
- Mas deste modo é mais completo e evita chamadas desnecessárias e diminui erros de unauthorized

```javascript
    const tokenUrl = insomnia.baseEnvironment.get('url') + '/login';

    const requestBody = {
    "login": "teste",
    "senha": "teste"
    };

    //"2025-05-05T02:15:31.738321753-03:00"
    const tokenExpiration = insomnia.baseEnvironment.get('token_expiration');

    const now = new Date(); // Data atual
    const expirationDate = tokenExpiration ? new Date(tokenExpiration) : new Date(0);

    console.log("expirationDate: " + expirationDate);

    if (!tokenExpiration || expirationDate < now) {

        const response = await fetch(tokenUrl, {
                method: 'POST',
                headers: {
                        'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
        });

        const jsonBody = await response.json();

        console.log("token response: " + JSON.stringify(jsonBody));

        insomnia.baseEnvironment.set("token_string", jsonBody.access_token);
        insomnia.baseEnvironment.set("token_expiration", jsonBody.expires_at);

    }
```
