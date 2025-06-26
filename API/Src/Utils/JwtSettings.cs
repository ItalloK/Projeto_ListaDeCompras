namespace ListasAPI.Src.Utils
{
    public class JwtSettings
    {
        public string SecretKey { get; set; } = Global.chaveJWT;
        public int ExpirationMinutes { get; set; } = 60;
        public string Issuer { get; set; } = "ListasAPI";
        public string Audience { get; set; } = "ListasClient";
    }
}
