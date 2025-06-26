using ListasAPI.Src.Data;
using ListasAPI.Src.Repositories;
using ListasAPI.Src.Services;
using ListasAPI.Src.Utils;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddScoped<IAuthService, AuthService>();
builder.Services.AddScoped<IUserRepository, UserRepository>();

// Chave secreta (coloque em config segura em produ��o!)
var key = Global.chaveJWT; // voc� pode mover isso para appsettings.json

DbInitializer.Initialize();

// Adiciona autentica��o JWT
builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.RequireHttpsMetadata = false;
    options.SaveToken = true;

    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuer = false, // Em produ��o, configure um issuer!
        ValidateAudience = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key))
    };
});

builder.Services.AddControllers();

var app = builder.Build();

// Middleware
app.UseHttpsRedirection();

// ATEN��O: a ordem importa � UseAuthentication antes de UseAuthorization!
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
