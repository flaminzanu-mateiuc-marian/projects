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

namespace cfr
{
    public partial class gestionare : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public gestionare()
        {
            InitializeComponent();
        }

        private void gestionare_FormClosing(object sender, FormClosingEventArgs e)
        {
            (new Form1()).Show();
            this.Hide();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            SqlCommand cmd = con.CreateCommand();
            cmd.CommandText = "select * from statii";
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            da.Fill(dt);
            for(int a=0; a<dt.Rows.Count; a++)
            {
                    string magistrale = dt.Rows[a][2].ToString();
                    string[] orase = new string[10 ^ 9];
                    string[] opriri = new string[10 ^ 9];
                    opriri = magistrale.Split('|');
                    int numar_magistrale = opriri.Count();
                    for (int i = 2; i < numar_magistrale; i += 2)
                    {
                        orase = opriri[i].Split(',');
                        int numar_orase = orase.Count();
                        for (int j = 0; j < numar_orase; j++)
                        {
                            cmd.CommandText = "select * from Orase where NumeOras = ('" + orase[j].Trim() + "')";
                            SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                            DataTable dt1 = new DataTable();
                            da1.Fill(dt1);
                            if (dt1.Rows.Count == 0)
                            {
                                cmd.CommandText = "insert into Orase values ('" + opriri[i - 1] + "','" + orase[j].Trim() + "')";
                                cmd.ExecuteNonQuery();
                            }
                        }

                    }
                }
                MessageBox.Show("Actualizarea a avut loc cu succes!");
            }
        

        private void gestionare_Load(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            (new stergere_utilizator()).Show();

        }

        private void button4_Click(object sender, EventArgs e)
        {
            (new stergere_tren()).Show();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            (new adaugare_tren()).Show();
        }
    }

}
