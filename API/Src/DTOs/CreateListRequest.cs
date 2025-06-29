namespace ListasAPI.Src.DTOs
{
    public class CreateListRequest
    {
        public string Title { get; set; }
        public List<ListItemRequest> Items { get; set; } = new();
        public string Email { get; set; }
    }

    public class ListItemRequest
    {
        public string Name { get; set; }
        public int Quantity { get; set; }
        public decimal Value { get; set; }
    }
}
