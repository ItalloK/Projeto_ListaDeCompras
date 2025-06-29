using ListasAPI.Src.Data;
using ListasAPI.Src.DTOs;
using ListasAPI.Src.Model;
using Microsoft.Data.Sqlite;
using System.Globalization;

namespace ListasAPI.Src.Repositories
{
    public class ListRepository : IListRepository
    {
        private readonly string _connectionString;

        public ListRepository()
        {
            _connectionString = DbInitializer.UsuariosConnectionString;
        }

        public async Task<string> GetUserRole(string email)
        {
            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            var cmd = connection.CreateCommand();
            cmd.CommandText = "SELECT Role FROM Users WHERE Email = @Email";
            cmd.Parameters.AddWithValue("@Email", email);

            var result = await cmd.ExecuteScalarAsync();
            return result?.ToString() ?? "Comum";
        }

        public async Task CreateList(ShoppingList list)
        {
            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            using var tx = connection.BeginTransaction();

            var insertList = connection.CreateCommand();
            insertList.CommandText = @"
                INSERT INTO Lists (Title, Code, CreatedAt, ExpiresAt, UserEmail)
                VALUES (@Title, @Code, @CreatedAt, @ExpiresAt, @UserEmail);
                SELECT last_insert_rowid();";

            insertList.Parameters.AddWithValue("@Title", list.Title);
            insertList.Parameters.AddWithValue("@Code", list.Code);
            insertList.Parameters.AddWithValue("@CreatedAt", list.CreatedAt.ToString("o"));
            insertList.Parameters.AddWithValue("@ExpiresAt", list.ExpiresAt.ToString("o"));
            insertList.Parameters.AddWithValue("@UserEmail", list.UserEmail);

            var listId = (long)await insertList.ExecuteScalarAsync();

            foreach (var item in list.Items)
            {
                var insertItem = connection.CreateCommand();
                insertItem.CommandText = @"
                    INSERT INTO Items (ListId, Name, Quantity, Value)
                    VALUES (@ListId, @Name, @Quantity, @Value)";
                insertItem.Parameters.AddWithValue("@ListId", listId);
                insertItem.Parameters.AddWithValue("@Name", item.Name);
                insertItem.Parameters.AddWithValue("@Quantity", item.Quantity);
                insertItem.Parameters.AddWithValue("@Value", item.Value.ToString(CultureInfo.InvariantCulture));

                await insertItem.ExecuteNonQueryAsync();
            }

            await tx.CommitAsync();
        }

        public async Task<List<ShoppingList>> GetListsByUser(string email)
        {
            var result = new List<ShoppingList>();

            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            var command = connection.CreateCommand();
            command.CommandText = "SELECT * FROM Lists WHERE UserEmail = @Email";
            command.Parameters.AddWithValue("@Email", email);

            using var reader = await command.ExecuteReaderAsync();
            while (await reader.ReadAsync())
            {
                var list = new ShoppingList
                {
                    Id = reader.GetInt32(0),
                    Title = reader.GetString(1),
                    Code = reader.GetString(2),
                    CreatedAt = DateTime.Parse(reader.GetString(3)),
                    ExpiresAt = DateTime.Parse(reader.GetString(4)),
                    UserEmail = reader.GetString(5),
                    Items = new List<ShoppingListItem>() // Corrigido aqui
                };

                result.Add(list);
            }

            foreach (var list in result)
            {
                var itemCommand = connection.CreateCommand();
                itemCommand.CommandText = "SELECT Id, ListId, Name, Quantity, Value FROM Items WHERE ListId = @ListId";
                itemCommand.Parameters.AddWithValue("@ListId", list.Id);

                using var itemReader = await itemCommand.ExecuteReaderAsync();
                while (await itemReader.ReadAsync())
                {
                    list.Items.Add(new ShoppingListItem
                    {
                        Id = itemReader.GetInt32(0),
                        ListId = itemReader.GetInt32(1),
                        Name = itemReader.GetString(2),
                        Quantity = itemReader.GetInt32(3),
                        Value = itemReader.GetDecimal(4)
                    });
                }
            }

            return result;
        }


        public async Task<ShoppingList> GetListByCode(string code)
        {
            ShoppingList list = null;

            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            var command = connection.CreateCommand();
            command.CommandText = "SELECT * FROM Lists WHERE Code = @Code";
            command.Parameters.AddWithValue("@Code", code);

            using var reader = await command.ExecuteReaderAsync();
            if (await reader.ReadAsync())
            {
                list = new ShoppingList
                {
                    Id = reader.GetInt32(0),
                    Title = reader.GetString(1),
                    Code = reader.GetString(2),
                    CreatedAt = DateTime.Parse(reader.GetString(3)),
                    ExpiresAt = DateTime.Parse(reader.GetString(4)),
                    UserEmail = reader.GetString(5),
                    Items = new List<ShoppingListItem>() // Corrigido aqui
                };
            }

            if (list != null)
            {
                var itemCommand = connection.CreateCommand();
                itemCommand.CommandText = "SELECT Id, ListId, Name, Quantity, Value FROM Items WHERE ListId = @ListId";
                itemCommand.Parameters.AddWithValue("@ListId", list.Id);

                using var itemReader = await itemCommand.ExecuteReaderAsync();
                while (await itemReader.ReadAsync())
                {
                    list.Items.Add(new ShoppingListItem
                    {
                        Id = itemReader.GetInt32(0),
                        ListId = itemReader.GetInt32(1),
                        Name = itemReader.GetString(2),
                        Quantity = itemReader.GetInt32(3),
                        Value = itemReader.GetDecimal(4)
                    });
                }
            }

            return list;
        }


        public async Task DeleteList(int id)
        {
            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            var deleteItems = connection.CreateCommand();
            deleteItems.CommandText = "DELETE FROM Items WHERE ListId = @Id";
            deleteItems.Parameters.AddWithValue("@Id", id);
            await deleteItems.ExecuteNonQueryAsync();

            var deleteList = connection.CreateCommand();
            deleteList.CommandText = "DELETE FROM Lists WHERE Id = @Id";
            deleteList.Parameters.AddWithValue("@Id", id);
            await deleteList.ExecuteNonQueryAsync();
        }

        public async Task UpdateList(int id, UpdateListRequest request)
        {
            using var connection = new SqliteConnection(_connectionString);
            await connection.OpenAsync();

            var updateTitle = connection.CreateCommand();
            updateTitle.CommandText = "UPDATE Lists SET Title = @Title WHERE Id = @Id";
            updateTitle.Parameters.AddWithValue("@Title", request.Title);
            updateTitle.Parameters.AddWithValue("@Id", id);
            await updateTitle.ExecuteNonQueryAsync();

            var deleteOldItems = connection.CreateCommand();
            deleteOldItems.CommandText = "DELETE FROM Items WHERE ListId = @Id";
            deleteOldItems.Parameters.AddWithValue("@Id", id);
            await deleteOldItems.ExecuteNonQueryAsync();

            foreach (var item in request.Items)
            {
                var insertItem = connection.CreateCommand();
                insertItem.CommandText = "INSERT INTO Items (ListId, Name, Quantity, Value) VALUES (@ListId, @Name, @Quantity, @Value)";
                insertItem.Parameters.AddWithValue("@ListId", id);
                insertItem.Parameters.AddWithValue("@Name", item.Name);
                insertItem.Parameters.AddWithValue("@Quantity", item.Quantity);
                insertItem.Parameters.AddWithValue("@Value", item.Value.ToString(CultureInfo.InvariantCulture));
                await insertItem.ExecuteNonQueryAsync();
            }
        }
    }
}