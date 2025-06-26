namespace ListasAPI.Src.Model
{
    public class LoginResponse
    {
        public bool status { get; set; }
        public string message { get; set; } = string.Empty;
        public string token { get; set; } = string.Empty;
        public string name { get; set; } = string.Empty;
        public string email { get; set; } = string.Empty;
        public string refresh_token { get; set; } = string.Empty;
        public string role { get; set; } = string.Empty;
    }
}
