using ListasAPI.Src.DTOs;
using ListasAPI.Src.Model;

namespace ListasAPI.Src.Repositories
{
    public interface IListRepository
    {
        Task CreateList(ShoppingList list);
        Task<string> GetUserRole(string email);
        Task<List<ShoppingList>> GetListsByUser(string email);
        Task<ShoppingList> GetListByCode(string code);
        Task DeleteList(int id);
        Task UpdateList(int id, UpdateListRequest request);
    }
}
