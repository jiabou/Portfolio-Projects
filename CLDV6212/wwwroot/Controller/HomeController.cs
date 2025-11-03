using Azure.Data.Tables;
using Azure.Storage.Blobs;
using Azure.Storage.Queues;
using Azure.Storage.File.Shares;
using ABCRetail.wwwroot.Models;
using Microsoft.Extensions.Configuration;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using System.Threading.Tasks;
namespace ABCRetail.wwwroot.Controller
{

    //Table Function using Controller
    public class RegisterController : Controller
    {
        private readonly IConfiguration _configuration;
        public RegisterController(IConfiguration configuration)
        {
            _configuration = configuration;
        }
        // GET: Register
        public async Task<IActionResult> Register()
        {
            string connectionString = _configuration.GetConnectionString("AzureTableStorage");
            var tableClient = new TableClient(connectionString, "Customers");
            await tableClient.CreateIfNotExistsAsync();
            List<CustomerEntity> customers = new List<CustomerEntity>();
            await foreach (var customer in tableClient.QueryAsync<CustomerEntity>())
            {
                customers.Add(customer);
            }
            return View(customers);
        }
        // GET: Register
        [HttpGet]
        public IActionResult Register()
        {
            return View();
        }
        // POST: Register
        [HttpPost]
        public async Task<IActionResult> Register(CustomerEntity customer)
        {
            if (ModelState.IsValid)
            {
                string connectionString = _configuration.GetConnectionString("AzureTableStorage");
                var tableClient = new TableClient(connectionString, "customers");
                await tableClient.CreateIfNotExistsAsync();
                await tableClient.AddEntityAsync(customer);
                // Set a success message
                TempData["SuccessMessage"] = "Customer information successfully stored!";
            return RedirectToAction(nameof(Index));
            }
            return View(customer);
        }

    }

    //Blob Function using Controller
    public class UploadProductController : Controller
    {
        [HttpPost("upload")]
        [ApiController]
        public async Task<IActionResult> UploadImage([FromForm] ImageEntity imageEntity)
        {
            if (imageEntity?.File == null || imageEntity.File.Length == 0)
            {
                return BadRequest("No file uploaded.");
            }

            // Create client to interact with Blob Service
            var blobServiceClient = new BlobServiceClient("AzureBlobStorage");
            var blobContainerClient = blobServiceClient.GetBlobContainerClient("productimages");

            // Create the container if it doesn't exist
            await blobContainerClient.CreateIfNotExistsAsync();

            // Generate a unique name for the file
            var blobName = Path.GetFileName(imageEntity.File.FileName);
            var blobClient = blobContainerClient.GetBlobClient(blobName);

            // Upload the file
            using (var stream = imageEntity.File.OpenReadStream())
            {
                await blobClient.UploadAsync(stream, true);
            }

            return Ok(new { fileName = blobName });
        }
    }

    //Queue Function using Controller
    public class QueueMessageController : Controller
    {
        public IActionResult Queue() => View();

        [HttpPost]
        public async Task<IActionResult> Queue(QueueEntity queueEntity)
        {
            if (!string.IsNullOrEmpty(queueEntity.Message))
            {
                // Send the message to Azure Queue Storage
                var queueClient = new QueueClient(connectionString, queueName);
                await queueClient.CreateIfNotExistsAsync();
                await queueClient.SendMessageAsync(queueEntity.Message);

                return RedirectToAction("Success");
            }
            return View(queueEntity);
        }

        public IActionResult Success() => View();
    }

    //File Share Function using Controller
    public class FileController : Controller
    {
        public IActionResult UploadFile() => View();

        [HttpPost]
        public async Task<IActionResult> UploadFile(FileShareEntity fileShareEntity)
        {
            if (fileShareEntity.File != null && fileShareEntity.File.Length > 0)
            {
                // Create a client to interact with the File Shares Service
                var shareClient = new ShareClient(connectionString, shareName);
                await shareClient.CreateIfNotExistsAsync();

                // Create a directory
                var directoryClient = shareClient.GetDirectoryClient("uploads");
                await directoryClient.CreateIfNotExistsAsync();

                // Upload the file
                var fileClient = directoryClient.GetFileClient(model.File.FileName);
                using (var stream = fileShareEntity.File.OpenReadStream())
                {
                    await fileClient.CreateAsync(model.File.Length);
                    await fileClient.UploadAsync(stream);
                }

                return RedirectToAction("Success");
            }

            return View(fileShareEntity);
        }

        public IActionResult Success() => View();
    }
}

