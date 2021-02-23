package com.example.kotlinfirstapp

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mIsShowPass = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {

            val nama = etNama.text.toString().trim()
            val email = etEmailReg.text.toString().trim()
            val nohp = etNoHP.text.toString().trim()
            val pass = etPasswordReg.text.toString().trim()


            if (nama.isEmpty()) {
                etNama.error = "Nama tidak boleh kosong!"
                etNama.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                etEmailReg.error = "E-mail tidak boleh kososng!"
                etEmailReg.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailReg.error = "E-mail tidak valid!"
                etEmailReg.requestFocus()
                return@setOnClickListener
            }

            if (nohp.isEmpty()) {
                etNoHP.error = "No Handphone tidak boleh kosong!"
                etNoHP.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty() || pass.length < 6) {
                etPasswordReg.error = "Password minimal 6 karakter!"
                etPasswordReg.requestFocus()
                return@setOnClickListener
            }

            registerUser(nama, email, nohp, pass)
        }

        tvLogin.setOnClickListener {
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
        ivShowHidePass.setOnClickListener {
            mIsShowPass = !mIsShowPass
            showPassword(mIsShowPass)
        }

        showPassword(mIsShowPass)

    }


    private fun registerUser(nama: String, email: String, nohp: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "akun Trashure telah berhasil dibuat", Toast.LENGTH_SHORT)
                        .show()

                    Intent(this@RegisterActivity, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this@RegisterActivity, HomeActivity::class.java).also { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun showPassword(isShow: Boolean) {
        if (isShow) {
            etPasswordReg.transformationMethod = HideReturnsTransformationMethod.getInstance()
            ivShowHidePass.setImageResource(R.drawable.ic_visibility)
        } else {
            etPasswordReg.transformationMethod = PasswordTransformationMethod.getInstance()
            ivShowHidePass.setImageResource(R.drawable.ic_visibilitynon)
        }
        etPasswordReg.setSelection(etPasswordReg.text.toString().length)
    }
}

