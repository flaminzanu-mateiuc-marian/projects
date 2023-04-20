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
    public partial class stergere_tren : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public stergere_tren()
        {
            InitializeComponent();
        }

        private void stergere_tren_Load(object sender, EventArgs e)
        {
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            SqlCommand cmd = con.CreateCommand();
            cmd.CommandText = "select NumarTren,LocPlecare,LocSosire,Via from curse";
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            da.Fill(dt);
            dataGridView1.DataSource = dt;
            DataGridViewButtonColumn col = new DataGridViewButtonColumn();
            dataGridView1.Columns.Add(col);
            col.Text = "Stergere";
            col.HeaderText = "Stergere";
            col.UseColumnTextForButtonValue = true;
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.ColumnIndex == 4)
            {
                SqlCommand cmd = con.CreateCommand();
                cmd.CommandText = "delete from curse where NumarTren=('" + dataGridView1.Rows[e.RowIndex].Cells[0].Value.ToString() + "')";
                cmd.ExecuteNonQuery();
                cmd.CommandText = "delete from statii where NumarTren=('" + dataGridView1.Rows[e.RowIndex].Cells[0].Value.ToString() + "')";
                cmd.ExecuteNonQuery();
                cmd.CommandText = "delete from preturi where NumarTren=('" + dataGridView1.Rows[e.RowIndex].Cells[0].Value.ToString() + "')";
                cmd.ExecuteNonQuery();
                MessageBox.Show("Stergerea a avut loc cu succes!");
                dataGridView1.Columns.Clear();
                cmd.CommandText = "select NumarTren,LocPlecare,LocSosire,Via from curse";
                SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                DataTable dt1 = new DataTable();
                da1.Fill(dt1);
                dataGridView1.DataSource = dt1;
                DataGridViewButtonColumn col = new DataGridViewButtonColumn();
                dataGridView1.Columns.Add(col);
                col.Text = "Stergere";
                col.HeaderText = "Stergere";
                col.UseColumnTextForButtonValue = true;
            }
        }
    }
}
