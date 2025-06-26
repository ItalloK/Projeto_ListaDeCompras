
# Informações sobre a API de Registro e Login

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
