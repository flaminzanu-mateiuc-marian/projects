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
    public partial class logare : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public logare()
        {
            
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == false)
            {
                SqlCommand cmd = con.CreateCommand();
                cmd.CommandText = "select TipUtilizator from utilizatori where NumePrenume=('" + textBox1.Text + "') and parola = ('"+textBox2.Text+"')";
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                if (dt.Rows.Count != 0)
                {
                    MessageBox.Show("V-ati autentificat cu succes!");
                    auxiliare.Logat = 1;
                    auxiliare.Nume = textBox1.Text;
                    int tipcont = Convert.ToInt32(cmd.ExecuteScalar());
                    auxiliare.Tipcont = tipcont;
                    (new Form1()).Show();
                    this.Hide();
                }
                else
                { MessageBox.Show("Campurile Nume Utilizatori si Parola nu corespund. Va rugam sa incercati din nou.");
                    textBox1.Text = "";
                    textBox2.Text = "";
                }

            }
            else
                MessageBox.Show("Va rugam sa completati toate campurile!");
        }

        private void logare_Load(object sender, EventArgs e)
        {
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            textBox2.PasswordChar = '*';
        }

        private void logare_FormClosing(object sender, FormClosingEventArgs e)
        {
            (new Form1()).Show();
            this.Hide();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            (new Form1()).Show();
            this.Hide();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            (new inregistrare()).Show();
            this.Hide();
        }
    }
}
