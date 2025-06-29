using ListasAPI.Src.DTOs;
using ListasAPI.Src.Model;
using ListasAPI.Src.Repositories;

namespace ListasAPI.Src.Services
{
    public class ListService : IListService
    {
        private readonly IListRepository _repo;

        public ListService(IListRepository repo)
        {
            _repo = repo;
        }

        public async Task<object> CreateList(CreateListRequest request)
        {
            string role = await _repo.GetUserRole(request.Email);
            DateTime now = DateTime.UtcNow;

            var list = new ShoppingList
            {
                Title = request.Title,
                Code = GenerateCode(),
                CreatedAt = now,
                ExpiresAt = role == "Prime" ? now.AddDays(30) : now.AddDays(7),
                UserEmail = request.Email,
                Items = request.Items.Select(i => new ShoppingListItem
                {
                    Name = i.Name,
                    Quantity = i.Quantity,
                    Value = i.Value
                }).ToList()
            };

            await _repo.CreateList(list);

            return new
            {
                status = true,
                message = "Lista criada com sucesso.",
                code = list.Code
            };
        }

        private string GenerateCode()
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            var random = new Random();
            return new string(Enumerable.Repeat(chars, 8).Select(s => s[random.Next(s.Length)]).ToArray());
        }

        public async Task<object> GetListsByUser(string email)
        {
            var lists = await _repo.GetListsByUser(email);
            return new { status = true, data = lists };
        }

        public async Task<object> GetListByCode(string code)
        {
            var list = await _repo.GetListByCode(code);
            if (list == null)
                return new { status = false, message = "Lista não encontrada." };
            return new { status = true, data = list };
        }

        public async Task<object> DeleteList(int id)
        {
            await _repo.DeleteList(id);
            return new { status = true, message = "Lista deletada com sucesso." };
        }

        public async Task<object> UpdateList(int id, UpdateListRequest request)
        {
            try
            {
                await _repo.UpdateList(id, request);
                return new { status = true, message = "Lista atualizada com sucesso." };
            }
            catch (KeyNotFoundException ex)
            {
                return new { status = false, message = ex.Message };
            }
            catch (Exception)
            {
                return new { status = false, message = "Erro inesperado ao atualizar a lista." };
            }
        }
    }
}
