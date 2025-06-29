namespace ListasAPI.Src.DTOs
{
    public class UpdateListRequest
    {
        public string Title { get; set; }
        public List<ListItemRequest> Items { get; set; }
    }
}
