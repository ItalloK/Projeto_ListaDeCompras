using ListasAPI.Src.DTOs;
using ListasAPI.Src.Model;
using ListasAPI.Src.Repositories;
using ListasAPI.Src.Utils;

namespace ListasAPI.Src.Services
{
    public class AuthService : IAuthService
    {
        private readonly IUserRepository _repo;

        public AuthService(IUserRepository repo)
        {
            _repo = repo;
        }

        public async Task<ApiResponse> Register(RegisterRequest request)
        {
            if (request.version != Global.serverVersion)
            {
                return new ApiResponse { status = false, message = "Versão incompatível." };
            }

            if (await _repo.UserExists(request.email))
            {
                return new ApiResponse { status = false, message = "Email já cadastrado." };
            }

            string passwordHash = BCrypt.Net.BCrypt.HashPassword(request.password);
            await _repo.CreateUser(request.name, request.email, passwordHash);

            Console.WriteLine($"[REGISTRO] '{request.email}' registrado com sucesso.");
            return new ApiResponse { status = true, message = "Registrado com sucesso!" };
        }

        public async Task<LoginResponse> Login(LoginRequest request)
        {
            if (request.version != Global.serverVersion)
            {
                return new LoginResponse { status = false, message = "Versão incompatível." };
            }

            var user = await _repo.GetUserByEmail(request.email);

            if (user == null || !BCrypt.Net.BCrypt.Verify(request.password, user.PasswordHash))
            {
                return new LoginResponse { status = false, message = "Login inválido!" };
            }

            string token = TokenService.GenerateAccessToken(request.email);
            string refreshToken = TokenService.GenerateRefreshToken();
            await _repo.SaveRefreshToken(request.email, refreshToken, DateTime.UtcNow.AddDays(7));

            Console.WriteLine($"[LOGIN] '{request.email}' logado com sucesso.");
            return new LoginResponse
            {
                status = true,
                message = "Login realizado com sucesso!",
                token = token,
                refresh_token = refreshToken,
                name = user.name,
                role = user.role,
                email = request.email
            };
        }

        public async Task<object> Refresh(RefreshRequest request)
        {
            var data = await _repo.GetRefreshToken(request.refreshToken);

            if (data == null)
            {
                Console.WriteLine($"[LOG] Token inválido ou expirado. (LN 74)");
                return new { status = false, message = "Token inválido ou expirado." };
            }

            var (nick, expiresAt, isRevoked) = data.Value;

            if (isRevoked || expiresAt < DateTime.UtcNow)
            {
                Console.WriteLine($"[LOG] Token inválido ou expirado. (LN 78)");
                return new { status = false, message = "Token inválido ou expirado." };
            }

            await _repo.RevokeRefreshToken(request.refreshToken);

            string newAccessToken = TokenService.GenerateAccessToken(nick);
            string newRefreshToken = TokenService.GenerateRefreshToken();
            await _repo.SaveRefreshToken(nick, newRefreshToken, DateTime.UtcNow.AddDays(7));

            return new
            {
                status = true,
                message = "Token renovado com sucesso.",
                token = newAccessToken,
                refresh_token = newRefreshToken
            };
        }
    }
}
