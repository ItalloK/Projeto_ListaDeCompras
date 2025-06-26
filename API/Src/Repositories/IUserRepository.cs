using ListasAPI.Src.DTOs;

namespace ListasAPI.Src.Repositories
{
    public interface IUserRepository
    {
        Task<bool> UserExists(string email);
        Task CreateUser(string nick, string email, string passwordHash);
        //Task<string?> GetPasswordHashByEmail(string email);
        Task<UserAuthData?> GetUserByEmail(string email);
        Task SaveRefreshToken(string email, string token, DateTime expiresAt);
        Task<(string Email, DateTime ExpiresAt, bool IsRevoked)?> GetRefreshToken(string token);
        Task RevokeRefreshToken(string token);
    }
}
