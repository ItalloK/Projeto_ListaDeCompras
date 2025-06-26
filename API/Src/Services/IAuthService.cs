using ListasAPI.Src.DTOs;
using ListasAPI.Src.Model;

namespace ListasAPI.Src.Services
{
    public interface IAuthService
    {
        Task<ApiResponse> Register(RegisterRequest request);
        Task<LoginResponse> Login(LoginRequest request);
        Task<object> Refresh(RefreshRequest request);
    }
}
