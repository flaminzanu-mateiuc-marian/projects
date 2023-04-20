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
    public partial class curse : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public curse()
        {
            InitializeComponent();
        }

        private void curse_Load(object sender, EventArgs e)
        {
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            SqlCommand cmd = con.CreateCommand();
            cmd.CommandText = "select * from curse";
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            da.Fill(dt);
            dataGridView1.DataSource = dt;
            dataGridView1.Columns[0].Width = 30;
            dataGridView1.Columns[1].Width = 30;
            dataGridView1.Columns[6].Width = 70;
            dataGridView1.Columns[7].Width = 70;
        }

        private void button1_Click(object sender, EventArgs e)
        {

            SqlCommand cmd = con.CreateCommand();
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            if (String.IsNullOrEmpty(textBox1.Text) == true && String.IsNullOrEmpty(textBox2.Text) == true && String.IsNullOrEmpty(textBox3.Text) == true)
            {
                cmd.CommandText = "select * from curse";
                da.Fill(dt);
                dataGridView1.DataSource = dt;
            }
            else
            {
                if (String.IsNullOrEmpty(textBox1.Text) == true && String.IsNullOrEmpty(textBox2.Text) == true && String.IsNullOrEmpty(textBox3.Text) == false)
                {
                    cmd.CommandText = "select * from curse where LocSosire LIKE('%" + textBox3.Text.Trim() + "%')";
                    da.Fill(dt);
                    dataGridView1.DataSource = dt;

                }
                else
                {
                    if (String.IsNullOrEmpty(textBox1.Text) == true && String.IsNullOrEmpty(textBox2.Text) == false && String.IsNullOrEmpty(textBox3.Text) == false)
                    {
                        cmd.CommandText = "select * from curse where LocSosire LIKE ('%" + textBox3.Text.Trim() + "%') and LocPlecare LIKE ('%" + textBox2.Text.Trim() + "%')";
                        da.Fill(dt);
                        dataGridView1.DataSource = dt;
                    }
                    else
                    {
                        if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == false && String.IsNullOrEmpty(textBox3.Text) == false)
                        {
                            cmd.CommandText = "select * from curse where LocSosire LIKE ('%" + textBox3.Text.Trim() + "%') and LocPlecare LIKE ('%" + textBox2.Text.Trim() + "%') and NumarTren LIKE('%" + textBox1.Text.Trim() + "%')";
                            da.Fill(dt);
                            dataGridView1.DataSource = dt;
                        }
                        else
                        {
                            if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == true && String.IsNullOrEmpty(textBox3.Text) == true)
                            {
                                cmd.CommandText = "select * from curse where NumarTren LIKE ('%" + textBox1.Text.Trim() + "%')";
                                da.Fill(dt);
                                dataGridView1.DataSource = dt;
                            }
                            else
                            {
                                if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == false && String.IsNullOrEmpty(textBox3.Text) == true)
                                {
                                    cmd.CommandText = "select * from curse where NumarTren LIKE('%" + textBox1.Text.Trim() + "%') and LocPlecare LIKE ('%" + textBox2.Text.Trim() + "%')";
                                    da.Fill(dt);
                                    dataGridView1.DataSource = dt;
                                }
                                else
                                {
                                    if (String.IsNullOrEmpty(textBox1.Text) == false && String.IsNullOrEmpty(textBox2.Text) == true && String.IsNullOrEmpty(textBox3.Text) == false)
                                    {
                                        cmd.CommandText = "select * from curse where NumarTren LIKE('%" + textBox1.Text.Trim() + "%') and LocSosire LIKE ('%" + textBox3.Text.Trim() + "%')";
                                        da.Fill(dt);
                                        dataGridView1.DataSource = dt;
                                    }
                                    else
                                    {
                                        if (String.IsNullOrEmpty(textBox1.Text) == true && String.IsNullOrEmpty(textBox2.Text) == false && String.IsNullOrEmpty(textBox3.Text) == true)
                                        {
                                            cmd.CommandText = "select * from curse where LocPlecare LIKE ('%" + textBox2.Text.Trim() + "%')";
                                            da.Fill(dt);
                                            dataGridView1.DataSource = dt;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex != -1)
            {
                SqlCommand cmd = con.CreateCommand();
                cmd.CommandText = "select * from statii where NumarTren=('" + dataGridView1.Rows[e.RowIndex].Cells[2].Value + "')";
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                dataGridView2.DataSource = dt;
                dataGridView2.Rows[0].Height = 109;
                dataGridView2.Columns[2].Width = 970;
                dataGridView2.Columns[1].Width = 92;
                dataGridView2.Columns[0].Width = 52;
            }
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
        }

        private void curse_FormClosing(object sender, FormClosingEventArgs e)
        {
            (new Form1()).Show();
            this.Hide();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            
            SqlCommand cmd = con.CreateCommand();
            cmd.CommandText = "select * from statii";
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            da.Fill(dt);

        }


    }
  }
