namespace ListasAPI.Src.DTOs
{
    public class UserAuthData
    {
        public string name { get; set; } = string.Empty;
        public string PasswordHash { get; set; } = string.Empty;
        public string role { get; set; } = string.Empty;
    }
}
