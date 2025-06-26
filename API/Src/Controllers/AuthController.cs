using ListasAPI.Src.Data;
using ListasAPI.Src.Model;
using ListasAPI.Src.Services;
using ListasAPI.Src.Utils;
using ListasAPI.Src.DTOs;
using Microsoft.AspNetCore.Mvc;
using System.Data.SQLite;

namespace ListasAPI.Src.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthService _authService;

        public AuthController(IAuthService authService)
        {
            _authService = authService;
            DbInitializer.Initialize(); // opcional: mover para Program.cs
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequest req)
            => Ok(await _authService.Register(req));

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest req)
            => Ok(await _authService.Login(req));

        [HttpPost("refresh")]
        public async Task<IActionResult> Refresh([FromBody] RefreshRequest req)
            => Ok(await _authService.Refresh(req));

    }
}