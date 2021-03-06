package com.peshale.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.peshale.nmedia.R
import com.peshale.nmedia.adapter.OnItemClickListener
import com.peshale.nmedia.adapter.PostAdapter
import com.peshale.nmedia.databinding.FragmentMainBinding
import com.peshale.nmedia.dto.Post
import com.peshale.nmedia.utils.AndroidUtils
import com.peshale.nmedia.utils.Arguments
import com.peshale.nmedia.vmodel.PostViewModel

class MainFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostAdapter(object : OnItemClickListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.toShareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onEdit(post: Post) {
                val bundle = Bundle().apply {
                    putString(Arguments.CONTENT, post.content)
                    putString(Arguments.VIDEO_LINK, post.video)
                    putLong(Arguments.POST_ID, post.id)
                }
                findNavController().navigate(R.id.action_mainFragment_to_editPostFragment, bundle)
            }

            override fun onDelete(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onView(post: Post) {
                viewModel.toViewById(post.id)
            }

            override fun onPlayVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                val playVideoValidation = context?.let { AndroidUtils.startIntent(it, intent) }
                if (playVideoValidation == false) {
                    Toast.makeText(
                        activity,
                        getString(R.string.error_play_video_validation),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                startActivity(intent)
            }

            override fun onPost(post: Post) {
                val bundle = Bundle().apply {
                    putParcelable("post", post)
                }
                findNavController().navigate(R.id.action_mainFragment_to_postCardFragment, bundle)
            }
        })

        binding.rvPosts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { posts ->
            adapter.submitList(posts)
        })

        binding.fabAddNewPost.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_newPostFragment)
        }
        return binding.root
    }
}