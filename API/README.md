
# Informações sobre as rotas da API

## 1. **Endpoints / Rotas da API**

### 1.1 **/api/auth/registro** - Registro de novos usuários

- **Método HTTP:** `POST`
- **Descrição:** Este endpoint permite que novos usuários se registrem na plataforma.
  
#### Corpo da requisição (JSON):
```json
{
    "name": "italo",
    "email": "italo@teste.com",
    "password": "italo123",
    "version": "1.0.0"
}
```

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "message": "Registrado com sucesso!"
}
```

### 1.2 **/api/auth/login** - Login de usuários

- **Método HTTP:** `POST`
- **Descrição:** Este endpoint permite que os usuários façam login na plataforma.
  
#### Corpo da requisição (JSON):
```json
{
    "email": "italo@teste.com",
    "password": "italo123",
    "version": "1.0.0"
}
```

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "message": "Login realizado com sucesso!",
    "token": "TOKEN_GERADO",
    "name": "italo",
    "email": "italo@teste.com",
    "refresh_token": "REFRESHTOKEN_GERADO",
    "role": "Comum" // Ou "Prime"
}
```

### 1.3 **/api/lists/create** - Criar Listas

- **Método HTTP:** `POST`
- **Descrição:** Este endpoint permite que os usuários criem as listas.
  
#### Corpo da requisição (JSON):
```json
{
    "title": "Titulo da Lista",
    "email": "italo@teste.com",
    "items": [
        { "name": "Item 1", "quantity": 1, "value": 29.50 },
        { "name": "Item 2", "quantity": 1, "value": 7.60 }
    ]
}
```

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "message": "Lista criada com sucesso.",
    "code": "XGREL1RE" // Codigo exemplo da Lista Criada
}
```

### 1.4 **/api/lists/update/{id}** - Atualizar Listas ( Apenas por ID )

- **Método HTTP:** `PUT`
- **Descrição:** Este endpoint permite que os usuários atualizem suas listas.
  
#### Corpo da requisição (JSON):
```json
{
  "title": "Titulo da Lista Atualizado",
  "email": "italo@teste.com",
  "items": [
    { "name": "Item Att 1", "quantity": 1, "value": 29.50 },
    { "name": "Item Att 2", "quantity": 1, "value": 7.60 }
  ]
}
```

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "message": "Lista atualizada com sucesso."
}
```

### 1.5 **/api/lists/delete/{id}** - Deletar Listas ( Apenas por ID )

- **Método HTTP:** `DELETE`
- **Descrição:** Este endpoint permite que os usuários deletem suas listas.

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "message": "Lista deletada com sucesso."
}
```

### 1.6 **/api/lists/email/{email}** - Buscar Listas por Email

- **Método HTTP:** `GET`
- **Descrição:** Este endpoint permite que os usuários busquem listas por email.

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "data": [
        {
            "id": 2,
            "title": "Titulo da Lista Atualizado",
            "code": "51PBG58N",
            "createdAt": "2025-06-29T14:04:39.481544-03:00",
            "expiresAt": "2025-07-06T14:04:39.481544-03:00",
            "userEmail": "italo@teste.com",
            "items": [
                {
                    "id": 11,
                    "listId": 2,
                    "name": "Item Att 1",
                    "quantity": 1,
                    "value": 29.5
                },
                {
                    "id": 12,
                    "listId": 2,
                    "name": "Item Att 2",
                    "quantity": 1,
                    "value": 7.6
                }
            ]
        },
        {
            "id": 3,
            "title": "Titulo da Lista",
            "code": "XGREL1RE",
            "createdAt": "2025-06-29T14:16:06.1286617-03:00",
            "expiresAt": "2025-07-06T14:16:06.1286617-03:00",
            "userEmail": "italo@teste.com",
            "items": [
                {
                    "id": 5,
                    "listId": 3,
                    "name": "Item 1",
                    "quantity": 1,
                    "value": 29.5
                },
                {
                    "id": 6,
                    "listId": 3,
                    "name": "Item 2",
                    "quantity": 1,
                    "value": 7.6
                }
            ]
        }
    ]
}
```

### 1.7 **/api/lists/code/{code}** - Buscar Lista por Código

- **Método HTTP:** `GET`
- **Descrição:** Este endpoint permite que os usuários busquem lista por codigo.

#### Corpo do retorno (JSON):
```json
{
    "status": true,
    "data": {
        "id": 3,
        "title": "Titulo da Lista",
        "code": "XGREL1RE",
        "createdAt": "2025-06-29T14:16:06.1286617-03:00",
        "expiresAt": "2025-07-06T14:16:06.1286617-03:00",
        "userEmail": "italo@teste.com",
        "items": [
            {
                "id": 5,
                "listId": 3,
                "name": "Item 1",
                "quantity": 1,
                "value": 29.5
            },
            {
                "id": 6,
                "listId": 3,
                "name": "Item 2",
                "quantity": 1,
                "value": 7.6
            }
        ]
    }
}
```