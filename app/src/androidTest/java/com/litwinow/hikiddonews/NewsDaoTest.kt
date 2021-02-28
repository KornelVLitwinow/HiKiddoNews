package com.litwinow.hikiddonews


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.litwinow.hikiddonews.dao.PostDao
import com.litwinow.hikiddonews.database.PostsDatabase
import com.litwinow.hikiddonews.model.Post
import org.junit.runner.RunWith
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class NewsDaoTest {
    private lateinit var database: PostsDatabase
    private lateinit var postDao: PostDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, PostsDatabase::class.java).build()
        postDao = database.postDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun get_posts_are_not_null_or_empty() = runBlocking {
        postDao.insertAllPosts(posts)
        val newsList = postDao.getPosts()
        assertNotNull(newsList)
        assertNotEquals(0, newsList)
    }

    @Test
    fun added_post_id_is_not_equal_null_or_zero() = runBlocking {
        val isAddedNews = postDao.addPost(firstPost)
        assertNotEquals(0, isAddedNews)
        assertNotNull(isAddedNews)
    }

    @Test
    fun find_post_by_id_return_not_null() = runBlocking {
        val id = postDao.addPost(secondPost)
        val foundNews = postDao.getPostById(id.toInt())
        assertNotNull(foundNews)
    }

    @Test
    fun update_post_return_new_title() = runBlocking {
        postDao.addPost(firstPost)
        var updatedPost = firstPost
        updatedPost = updatedPost.copy(title = "new title post")
        postDao.addPost(updatedPost)
        assertNotEquals(firstPost.title, updatedPost.title)
    }

    private var firstPost = Post(
        1,
        "były przewodniczący Rady Europejskiej, przewodniczący Europejskiej Partii Ludowej Donald Tusk. Były prezydent Polski - Donald Tusk. Były prezydent Polski - Donald Tusk. W wywiadzie dla najnowszego wydania \"Polityki\" Tusk, analizując przegraną kandydata",
        "https://i.iplsc.com/byly-prezydent-polski-donald-tusk/000AFOWO8C2A8ARY-C123-F4.webp",
        "Donald Tusk ostro o opozycji: Dla bojących się nie ma litości"
    )
    private val secondPost = Post(
        2,
        "Łukasz Szumowski aktualnie wypoczywa na wakacjach. Były minister zdrowia wybrał hiszpańską wyspę Fuerteventu",
        "https://gfx.wiadomosci.radiozet.pl/var/radiozetwiadomosci/storage/images/polska/polityka/lukasz-szumowski-na-wakacjach-byly-minister-wybral-sie-na-hiszpanska-wyspe-fuerteventure/8166737-1-pol-PL/Lukasz-Szumowski-na-wakacjach.-Byly-minister-wybral-sie-na-hiszpanska-wyspe_article_north.webp",
        "Łukasz Szumowski na wakacjach. Były minister wybrał się na hiszpańską wyspę"
    )
    private val thirdPost = Post(
        3,
        "Napięcia w PO nie ułatwiają Rafałowi Trzaskowskiemu budowy nowego \"ruchu obywatelskiego\". Jego zręby powstają w partyjnej siedzibie przy ulicy Wiejskiej w Warszawie.",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Rafal_Trzaskowski_2020_Bytom.jpg/180px-Rafal_Trzaskowski_2020_Bytom.jpg",
        "Rafał Trzaskowski wrócił z urlopu. Tworzy ruch w cieniu sporów w PO"
    )

    private val posts = (listOf(firstPost, secondPost, thirdPost))
}