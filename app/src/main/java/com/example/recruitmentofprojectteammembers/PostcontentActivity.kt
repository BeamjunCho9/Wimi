package com.example.recruitmentofprojectteammembers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.recruitmentofprojectteammembers.databinding.ActivityPostcontentBinding
import data.PostData
import data.PostModel
import data.PostStatus
import network.RetrofitClient.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var binding: ActivityPostcontentBinding

class PostcontentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postcontent)

        var postTitle : String
        var postContent : String

        binding = ActivityPostcontentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 게시물 등록 버튼 클릭 동작
        binding.pcBtnPost.setOnClickListener(){
            postTitle = binding.pcEdtTitle.text.toString()
            postContent = binding.pcEdtContent.text.toString()

            // 제목을 입력하지 않은 경우
            if (postTitle == ""){
                Toast.makeText(this@PostcontentActivity, "제목을 입력하세요!", Toast.LENGTH_SHORT).show()
            }
            // 내용을 입력하지 않은 경우
            else if(postContent == ""){
                Toast.makeText(this@PostcontentActivity, "내용을 입력하세요", Toast.LENGTH_SHORT).show()
            }
            else {
                // 게시물 등록
                retrofitService.requestPosting(PostData(loginResponse.member_id, postTitle, postContent)).enqueue(object : Callback<PostStatus>{
                    override fun onResponse(call: Call<PostStatus>, response: Response<PostStatus>) {
                        // 정상적으로 등록된 경우
                        if (response.body() == PostStatus("success")){
                            Toast.makeText(this@PostcontentActivity, "게시물 등록 완료!", Toast.LENGTH_SHORT).show()
                            val returnIntent = Intent()
                            returnIntent.putExtra("postTitle", postTitle)
                            returnIntent.putExtra("postContent", postContent)
                            setResult(Activity.RESULT_OK, returnIntent)
                            finish()
                        }
                        // 오류가 발생한 경우
                        else if (response.body() == PostStatus("error")){
                            var dialog = AlertDialog.Builder(this@PostcontentActivity)
                            dialog.setTitle("다시 시도해주세요.")
                            dialog.setMessage("")
                            dialog.show()
                        }
                    }

                    override fun onFailure(call: Call<PostStatus>, t: Throwable) {
                        Toast.makeText(this@PostcontentActivity, "에러! 다시 시도 ㄱㄱ", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }
}
