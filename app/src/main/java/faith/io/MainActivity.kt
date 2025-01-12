package faith.io

import Apiclient
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import faith.io.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvRecycler.layoutManager=LinearLayoutManager(this)
        fetchPosts()
    }

    fun fetchPosts(){
        val apiClient=Apiclient.buildApiClient(PostApiInterface::class.java)
        val request = apiClient.getPosts()
        request.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts = response.body()
                    posts?.let {
                        val adapterPost = AdapterPost(it,this@MainActivity)
                        binding.rvRecycler.adapter = adapterPost
                        Toast.makeText(this@MainActivity, "Fetched ${posts.size} posts", Toast.LENGTH_LONG).show()
                    } ?: run {
                        Toast.makeText(this@MainActivity, "No posts found", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch posts", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
               Toast.makeText(this@MainActivity,t.message.toString(),Toast.LENGTH_LONG).show()
            }

        })
    }
}