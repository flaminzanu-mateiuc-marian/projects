using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;
using System.Text.RegularExpressions;

namespace cfr
{

    public partial class inregistrare : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public inregistrare()
        {
            InitializeComponent();
        }

        private void inregistrare_Load(object sender, EventArgs e)
        {
            textBox3.PasswordChar = '*';
            textBox4.PasswordChar = '*';
            if (auxiliare.Tipcont == 1)
            {
                radioButton1.Visible = true;
                radioButton2.Visible = true;
                label5.Visible = true;
            }
            else
                if (auxiliare.Tipcont == 0 || auxiliare.Tipcont == 2)
                {
                    radioButton1.Visible = false;
                    radioButton2.Visible = false;
                    label5.Visible = false;
                    radioButton2.Checked = true;
                }
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            (new logare()).Show();
            this.Hide();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            foreach (Control x in this.Controls)
            {
                if (x is TextBox)
                    x.Text = "";
                if (x is RadioButton)
                {
                    if (auxiliare.Tipcont == 1)
                    {
                        radioButton1.Checked = false;
                        radioButton2.Checked = false;
                    }
                    else
                        if (auxiliare.Tipcont == 0 || auxiliare.Tipcont == 2)
                        {
                            radioButton1.Checked = false;
                            radioButton2.Checked = true;
                        }


                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == false && String.IsNullOrEmpty(textBox3.Text) == false && String.IsNullOrEmpty(textBox4.Text) == false)
            {
                SqlCommand cmd = con.CreateCommand();
                cmd.CommandText = "select * from utilizatori where Email=('" + textBox2.Text + "')";
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                if (Regex.IsMatch(textBox2.Text, @"^(?("")("".+?(?<!\\)""@)|(([0-9a-z]((\.(?!\.))|[-!#\$%&'\*\+/=\?\^`\{\}\|~\w])*)(?<=[0-9a-z])@))" + @"(?(\[)(\[(\d{1,3}\.){3}\d{1,3}\])|(([0-9a-z][-0-9a-z]*[0-9a-z]*\.)+[a-z0-9][\-a-z0-9]{0,22}[a-z0-9]))$") == true)
                {
                    if (dt.Rows.Count == 0)
                    {
                        if (textBox3.Text == textBox4.Text)
                        {
                            if (radioButton1.Checked == true || radioButton2.Checked == true)
                            {
                                if (radioButton1.Checked == true)
                                {
                                    SqlCommand cmd1 = con.CreateCommand();
                                    cmd1.CommandText = "insert into utilizatori(NumePrenume,Email,Parola,TipUtilizator) values (@NumePrenume, @Email, @Parola, @TipUtilizator)";
                                    cmd1.Parameters.AddWithValue("@NumePrenume", textBox1.Text);
                                    cmd1.Parameters.AddWithValue("@Email", textBox2.Text);
                                    cmd1.Parameters.AddWithValue("@Parola", textBox3.Text);
                                    cmd1.Parameters.AddWithValue("@TipUtilizator", 1);
                                    cmd1.ExecuteNonQuery();
                                    MessageBox.Show("Inregistrarea a avut loc cu succes. Va rugam sa va autentificati!");
                                    (new logare()).Show();
                                    this.Hide();
                                }
                                else
                                    if (radioButton2.Checked == true)
                                    {
                                        SqlCommand cmd1 = con.CreateCommand();
                                        cmd1.CommandText = "insert into utilizatori(NumePrenume,Email,Parola,TipUtilizator) values (@NumePrenume, @Email, @Parola, @TipUtilizator)";
                                        cmd1.Parameters.AddWithValue("@NumePrenume", textBox1.Text);
                                        cmd1.Parameters.AddWithValue("@Email", textBox2.Text);
                                        cmd1.Parameters.AddWithValue("@Parola", textBox3.Text);
                                        cmd1.Parameters.AddWithValue("@TipUtilizator", 2);
                                        cmd1.ExecuteNonQuery();
                                        MessageBox.Show("Inregistrarea a avut loc cu succes. Va rugam sa va autentificati!");
                                        (new logare()).Show();
                                        this.Hide();
                                    }
                            }
                            else
                            {
                                if (auxiliare.Tipcont == 1)
                                {
                                    MessageBox.Show("Va rugam sa bifati una dintre optiunile referitoare la tipul de cont!");
                                }
                            }
                        }
                        else
                        {
                            MessageBox.Show("Campurile Parola si Confirmare Parola nu corespund!");
                            textBox3.Text = "";
                            textBox4.Text = "";

                        }
                    }
                    else
                    {
                        MessageBox.Show("Adresa de email este deja in uz!");
                        textBox2.Text = "";
                        
                    }
                }
                else
                {
                    MessageBox.Show("Adresa de email este invalida!");
                    textBox2.Text = "";
                }
            }
            else
                MessageBox.Show("Va rugam sa completati toate campurile!");
        }

        private void inregistrare_FormClosing(object sender, FormClosingEventArgs e)
        {
            (new logare()).Show();
            this.Hide();
        }
    }
}
