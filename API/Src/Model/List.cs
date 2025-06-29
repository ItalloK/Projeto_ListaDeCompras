namespace ListasAPI.Src.Model
{
    public class ShoppingList
    {
        public int Id { get; set; }
        public string Title { get; set; }
        public string Code { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime ExpiresAt { get; set; }
        public string UserEmail { get; set; }
        public List<ShoppingListItem> Items { get; set; } = new();
    }

    public class ShoppingListItem
    {
        public int Id { get; set; }
        public int ListId { get; set; }
        public string Name { get; set; }
        public int Quantity { get; set; }
        public decimal Value { get; set; }
    }
}
