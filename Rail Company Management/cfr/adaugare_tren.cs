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
    public partial class adaugare_tren : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public adaugare_tren()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            SqlCommand cmd1 = con.CreateCommand();
            try 
            {
                cmd1.CommandText = "insert into curse values (@Tip, @NumarTren, @LocPlecare, @LocSosire, @Via, @OraPlecare, @OraSosire, @Dotari, @Operator)";
                cmd1.Parameters.AddWithValue("@Tip", txtTip.Text);
                cmd1.Parameters.AddWithValue("@NumarTren", Convert.ToInt32(txtNrTren.Text));
                cmd1.Parameters.AddWithValue("@LocPlecare", txtLocPlec.Text);
                cmd1.Parameters.AddWithValue("@LocSosire", txtLocSos.Text);
                cmd1.Parameters.AddWithValue("@Via", txtVia.Text);
                cmd1.Parameters.AddWithValue("@OraPlecare", txtOraPlecare.Text);
                cmd1.Parameters.AddWithValue("@OraSosire", txtOraSosire.Text);
                cmd1.Parameters.AddWithValue("@Dotari", txtDotari.Text);
                cmd1.Parameters.AddWithValue("@Operator", txtOperator.Text);
                cmd1.ExecuteNonQuery();
            }
            catch(System.Data.DataException x)
            {
                MessageBox.Show(x.ToString());
            }

            SqlCommand cmd2 = con.CreateCommand();
            try
            {
                cmd2.CommandText = "insert into statii values (@NumarTren, @Magistrale, @Statii)";
                cmd2.Parameters.AddWithValue("@NumarTren", txtNrTren.Text);
                cmd2.Parameters.AddWithValue("@Magistrale", txtMagistrale.Text);
                cmd2.Parameters.AddWithValue("@Statii", txtStatii.Text);
                cmd2.ExecuteNonQuery();
            }
            catch (System.Data.DataException x)
            {
                MessageBox.Show(x.ToString());
            }

            SqlCommand cmd3 = con.CreateCommand();
            try
            {
                cmd3.CommandText = "insert into preturi values (@NumarTren, @Pret1, @Pret2_Cu_Loc, @Pret2_Fara_Loc, @Pret_Cuseta, @Pret_DormitSingle, @Pret_DormitDublu)";
                cmd3.Parameters.AddWithValue("@NumarTren", Convert.ToInt32(txtNrTren.Text));


                if(String.IsNullOrEmpty(txtClasa1.Text)==true)
                    cmd3.Parameters.AddWithValue("@Pret1", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret1", float.Parse(txtClasa1.Text));


                if (String.IsNullOrEmpty(txtClasa2Cu.Text)==true)
                    cmd3.Parameters.AddWithValue("@Pret2_Cu_Loc", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret2_Cu_Loc", float.Parse(txtClasa2Cu.Text));


                if (String.IsNullOrEmpty(txtClasa2Fara.Text)==true)
                    cmd3.Parameters.AddWithValue("@Pret2_Fara_Loc", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret2_Fara_Loc", float.Parse(txtClasa2Fara.Text));


                if (String.IsNullOrEmpty(txtCuseta.Text)==true)
                    cmd3.Parameters.AddWithValue("@Pret_Cuseta", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret_Cuseta", float.Parse(txtCuseta.Text));


                if (String.IsNullOrEmpty(txtDormit1.Text) == true)
                    cmd3.Parameters.AddWithValue("@Pret_DormitSingle", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret_DormitSingle", float.Parse(txtDormit1.Text));


                if (String.IsNullOrEmpty(txtDormit2.Text) == true)
                    cmd3.Parameters.AddWithValue("@Pret_DormitDublu", 0);
                else
                    cmd3.Parameters.AddWithValue("@Pret_DormitDublu", float.Parse(txtDormit2.Text));
                cmd3.ExecuteNonQuery();
            }
            catch (System.Data.DataException x)
            {
                MessageBox.Show(x.ToString());
            }
            MessageBox.Show("Inregistrarea a avut loc cu succes!");
        }
        private void validare()
        {
            if (String.IsNullOrEmpty(txtTip.Text) == false && String.IsNullOrEmpty(txtNrTren.Text) == false && String.IsNullOrEmpty(txtLocPlec.Text) == false && String.IsNullOrEmpty(txtLocSos.Text) == false && String.IsNullOrEmpty(txtOraSosire.Text) == false && String.IsNullOrEmpty(txtOraPlecare.Text) == false && String.IsNullOrEmpty(txtDotari.Text) == false && String.IsNullOrEmpty(txtOperator.Text) == false && String.IsNullOrEmpty(txtMagistrale.Text) == false && String.IsNullOrEmpty(txtStatii.Text) == false)
            {
                button1.Enabled = true;
            }
            else
                button1.Enabled = false;

                
        }

        private void txtDormit2_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtOraPlecare_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtOraSosire_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtLocPlec_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtLocSos_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtDotari_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtOperator_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtVia_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtMagistrale_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtStatii_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtClasa1_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtClasa2Cu_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtClasa2Fara_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtCuseta_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtDormit1_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtNrTren_TextChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void txtTip_TextChanged(object sender, EventArgs e)
        {
            validare();
        }
    }
}
