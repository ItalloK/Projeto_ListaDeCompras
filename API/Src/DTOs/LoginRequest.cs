﻿namespace ListasAPI.Src.DTOs
{
    public class LoginRequest
    {
        public string email { get; set; } = string.Empty;
        public string password { get; set; } = string.Empty;
        public string version { get; set; } = string.Empty;
    }
}
