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
    public partial class Form1 : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            label1.Text = "";
            if (auxiliare.Logat == 0)
            {
                label1.Text = "Utilizator neautentificat";
                auxiliare.Tipcont = 0;
                
            }
            else
                label1.Text = "Bine ati venit, " + auxiliare.Nume;
            if(con.State == ConnectionState.Open)
                con.Close();
            if (auxiliare.Tipcont == 1)
                button7.Visible = true;
            else
                button7.Visible = false;
            con.Open();
            Random rnd = new Random();
            int index = rnd.Next(1, 6);
            panel2.BackgroundImage = Image.FromFile(@"imagini/" + index + ".jpg");
            panel2.BackgroundImageLayout = ImageLayout.Stretch;
            
        }

        private void button2_Click(object sender, EventArgs e)
        {
            (new curse()).Show();
            this.Hide();
        }


        private void button6_Click(object sender, EventArgs e)
        {
            if ((MessageBox.Show("Doriti sa inchdeti aplicatia?", "Inchidere", MessageBoxButtons.YesNo, MessageBoxIcon.Hand)) == DialogResult.Yes)
            {
                Environment.Exit(1);
            }
        }


       

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            if ((MessageBox.Show("Doriti sa inchdeti aplicatia?", "Inchidere", MessageBoxButtons.YesNo, MessageBoxIcon.Hand)) == DialogResult.No)
            {
                e.Cancel = true;
            }
            
        }

        private void button3_Click_1(object sender, EventArgs e)
        {
            (new harta()).Show();
        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            (new planificator()).Show();
            this.Hide();
        }

        private void button4_Click_1(object sender, EventArgs e)
        {
            (new logare()).Show();
            this.Hide();
        }

        private void button7_Click_1(object sender, EventArgs e)
        {
            (new gestionare()).Show();
            this.Hide();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            MessageBox.Show("Aici ar trebui sa se deschida mersul trenurilor, dar nu poate fi incarcat pe github din motive de dimensiune.");
        }
    }
}

