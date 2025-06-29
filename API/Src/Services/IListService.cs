using ListasAPI.Src.DTOs;

namespace ListasAPI.Src.Services
{
    public interface IListService
    {
        Task<object> CreateList(CreateListRequest request);
        Task<object> GetListsByUser(string email);
        Task<object> GetListByCode(string code);
        Task<object> DeleteList(int id);
        Task<object> UpdateList(int id, UpdateListRequest request);
    }
}
