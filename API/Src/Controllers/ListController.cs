using ListasAPI.Src.DTOs;
using ListasAPI.Src.Services;
using Microsoft.AspNetCore.Mvc;

namespace ListasAPI.Src.Controllers
{
    [ApiController]
    [Route("api/lists")]
    public class ListController : ControllerBase
    {
        private readonly IListService _service;

        public ListController(IListService service)
        {
            _service = service;
        }

        [HttpPost("create")]
        public async Task<IActionResult> Create([FromBody] CreateListRequest request)
            => Ok(await _service.CreateList(request));

        [HttpGet("user/{email}")]
        public async Task<IActionResult> GetByUser(string email)
            => Ok(await _service.GetListsByUser(email));

        [HttpGet("code/{code}")]
        public async Task<IActionResult> GetByCode(string code)
            => Ok(await _service.GetListByCode(code));

        [HttpDelete("delete/{id}")]
        public async Task<IActionResult> Delete(int id)
            => Ok(await _service.DeleteList(id));

        [HttpPut("update/{id}")]
        public async Task<IActionResult> Update(int id, [FromBody] UpdateListRequest request)
            => Ok(await _service.UpdateList(id, request));
    }
}
