namespace ListasAPI.Src.Model
{
    public class InfoUserResponse
    {
        public bool status;
        public string name { get; set; } = string.Empty;
        public string email { get; set; } = string.Empty;
        public string role { get; set; } = string.Empty;
    }
}
