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
    public partial class planificator : Form
    {
        SqlConnection con = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=|DataDirectory|cfr.mdf;Integrated Security=True");
        public int Gata = 0;
        public int NumarTren = 0;
        public int NumarTren1 = 0;
        public int NumarTren2 = 0;
        public string TipTren;
        public string TipTren1;
        public string TipTren2;
        public int Nr_statii = 0;
        public int Nr_statii_tot = 0;
        public string clasa = "";
        public string dusintors = "";
        public string varsta = "";
        public string via = "";
        public string via1 = "";
        public string via2 = "";
        public string oper = "";
        public string oper1 = "";
        public string oper2 = "";
        string schimb = "";
        List<string> vector = new List<string>();
        public planificator()
        {
            InitializeComponent();
        }

        private void planificator_FormClosing(object sender, FormClosingEventArgs e)
        {
            (new Form1()).Show();
            this.Hide();
        }

        private void planificator_Load(object sender, EventArgs e)
        {
            lblAutentificare.Text = "Nu sunteti inregistrat in aplicatie. Pentru a putea genera "+Environment.NewLine+"preturi si a cumpara bilete, va rugam sa va autentificati!";
            if(auxiliare.Logat == 0)
            {
                panel1.Visible = false;
                panel2.Visible = false;
                panel3.Visible = false;
                panel4.Visible = false;
                lblAutentificare.Visible = true;
                btnAutentificare.Visible = true;
                textBox1.Visible = false;
            }
            else
            {
                panel1.Visible = true;
                panel2.Visible = true;
                panel3.Visible = true;
                panel4.Visible = true;
                lblAutentificare.Visible = false;
                btnAutentificare.Visible = false;
                textBox1.Visible = true;
            }
            if (con.State == ConnectionState.Open)
                con.Close();
            con.Open();
            button1.Enabled = false;
            button2.Enabled = false;
            button3.Enabled = false;
            comboBox3.Enabled = false;
            SqlCommand cmd = con.CreateCommand();
            cmd.CommandText = "select NumeOras from Orase";
            SqlDataAdapter da = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            da.Fill(dt);
            for (int i = 0; i < dt.Rows.Count; i++)
            {
                comboBox1.Items.Add(dt.Rows[i][0]);
                comboBox2.Items.Add(dt.Rows[i][0]);
            }
            cmd.CommandText = "select NumarTren from curse";
            SqlDataAdapter da1 = new SqlDataAdapter(cmd);
            DataTable dt1 = new DataTable();
            da1.Fill(dt1);
            for (int i = 0; i < dt1.Rows.Count; i++)
            {
                comboBox3.Items.Add(dt1.Rows[i][0]);
            }
        }

        private void validare_cautare()
        {
            if ((String.IsNullOrEmpty(comboBox1.Text) == false && String.IsNullOrEmpty(comboBox2.Text) == false && checkBox1.Checked == false) || (checkBox1.Checked == true && String.IsNullOrEmpty(comboBox3.Text)) == false)
            {
                button1.Enabled = true;

            }
            else
            {
                button1.Enabled = false;

            }
        }



        private void button1_Click(object sender, EventArgs e)
        {
            Nr_statii = 0;
            Nr_statii_tot = 0;
            string traseu = "";
            string traseu1 = "";
            string traseu2 = "";
            NumarTren = 0;
            NumarTren1 = 0;
            NumarTren2 = 0;
            TipTren = "";
            TipTren1 = "";
            TipTren2 = "";
            via = "";
            via1 = "";
            via2 = "";
            string oper = "";
            string oper1 = "";
            string oper2 = "";
            string intermediar = "";
            string plecsos = comboBox1.Text.ToString() + comboBox2.Text.ToString();
            int verif = 0;
            SqlCommand cmd = con.CreateCommand();
            Gata = 0;


            #region tren direct
            if (checkBox1.Checked == true && String.IsNullOrEmpty(comboBox1.Text) == false && String.IsNullOrEmpty(comboBox2.Text) == false)
            {
                cmd.CommandText = "select Curse.TipTren, Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii, Curse.Via,Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Curse.NumarTren=('" + comboBox3.Text + "') and  Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%')";
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                if (dt.Rows.Count != 0)
                {
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read() && Gata == 0)
                        {
                            vector.Clear();
                            traseu = "";
                            NumarTren = Convert.ToInt32(dr[1].ToString());
                            TipTren = dr[0].ToString();
                            via = dr[7].ToString();
                            oper = dr[8].ToString();
                            string magistrale = dr[6].ToString();
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
                                    vector.Add(orase[j].Trim());
                                    if (orase[j].Trim() == comboBox1.Text.Trim() || orase[j].Trim() == comboBox2.Text.Trim())
                                        traseu += orase[j].Trim().ToString();
                                }
                            }
                            if (plecsos == traseu)
                            {
                                Gata = 1;
                                break;
                            }
                        }
                    }
                    if (plecsos == traseu && Gata == 1)
                    {
                        cmd.CommandText = "select Curse.TipTren, Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire, Curse.Via, Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%') and Curse.NumarTren=('" + NumarTren + "')";
                        SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                        DataTable dt1 = new DataTable();
                        da1.Fill(dt1);
                        dataGridView1.DataSource = dt1;
                        Gata = 1;
                    }
                    Nr_statii_tot = vector.Count();
                    int index1 = vector.IndexOf(comboBox1.Text.Trim());
                    int index2 = vector.IndexOf(comboBox2.Text.Trim());
                    Nr_statii = index2 - index1;

                    //MessageBox.Show(Nr_statii.ToString());
                }
            }
            else
                if (checkBox1.Checked == false)
                {
                    if (Gata == 0)
                    {
                        cmd.CommandText = "select Curse.TipTren, Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii, Curse.Via,Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%')";
                        SqlDataAdapter da = new SqlDataAdapter(cmd);
                        DataTable dt = new DataTable();
                        da.Fill(dt);
                        if (dt.Rows.Count != 0)
                        {
                            using (SqlDataReader dr = cmd.ExecuteReader())
                            {
                                while (dr.Read() && Gata == 0)
                                {
                                    vector.Clear();
                                    traseu = "";
                                    NumarTren = Convert.ToInt32(dr[1].ToString());
                                    TipTren = dr[0].ToString();
                                    via = dr[7].ToString();
                                    oper = dr[8].ToString();
                                    string magistrale = dr[6].ToString();
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
                                            vector.Add(orase[j].Trim());
                                            if (orase[j].Trim() == comboBox1.Text.Trim() || orase[j].Trim() == comboBox2.Text.Trim())
                                                traseu += orase[j].Trim().ToString();
                                        }
                                    }
                                    if (plecsos == traseu)
                                    {
                                        Gata = 1;
                                        break;
                                    }
                                }
                            }
                            if (plecsos == traseu && Gata == 1)
                            {
                                cmd.CommandText = "select Curse.TipTren, Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire, Curse.Via, Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%') and Curse.NumarTren=('" + NumarTren + "')";
                                SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                                DataTable dt1 = new DataTable();
                                da1.Fill(dt1);
                                dataGridView1.DataSource = dt1;
                                Gata = 1;
                            }
                            Nr_statii_tot = vector.Count();
                            int index1 = vector.IndexOf(comboBox1.Text.Trim());
                            int index2 = vector.IndexOf(comboBox2.Text.Trim());
                            Nr_statii = index2 - index1;

                            //MessageBox.Show(Nr_statii.ToString());
                        }

                    }
                    /* 
                    else
                        if (String.IsNullOrEmpty(comboBox3.Text) == false && Gata == 0)
                        {
                            cmd.CommandText = "select Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%') and Statii.Statii LIKE ('%" + comboBox3.Text + "%')";
                            SqlDataAdapter da = new SqlDataAdapter(cmd);
                            DataTable dt = new DataTable();
                            da.Fill(dt);
                            if (dt.Rows.Count != 0)
                            {
                                using (SqlDataReader dr = cmd.ExecuteReader())
                                {
                                    while (dr.Read() && Gata == 0)
                                    {
                                        traseu1 = "";
                                        traseu2 = "";
                                        NumarTren = Convert.ToInt32(dr[0].ToString());
                                        string magistrale = dr[5].ToString();
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
                                                if (orase[j].Trim() == comboBox1.Text.Trim() || orase[j].Trim() == comboBox2.Text.Trim())
                                                    traseu += orase[j].Trim().ToString();
                                            }
                                        }
                                        if (plecsos == traseu)
                                        {
                                            Gata = 1;
                                            cmd.CommandText = "select Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%') and Curse.NumarTren=('" + NumarTren + "') and Statii.Statii LIKE ('%" + comboBox3.Text + "%')";
                                            break;
                                        }
                                    }
                                }
                                if (plecsos == traseu && Gata == 1)
                                {
                                    SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                                    DataTable dt1 = new DataTable();
                                    da1.Fill(dt1);
                                    dataGridView1.DataSource = dt1;
                                    Gata = 1;
                                }
                            }
                        }
                    */
            #endregion tren direct

                    #region Un schimb
                    if (Gata == 0)
                    {
                        //Pasul 1: gaseste primul oras care are legatura catre destinatie
                        cmd.CommandText = "select Curse.TipTren, Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii, Curse.Via, Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%')";
                        SqlDataAdapter da = new SqlDataAdapter(cmd);
                        DataTable dt = new DataTable();
                        da.Fill(dt);
                        dataGridView1.DataSource = dt;
                        if (dt.Rows.Count != 0)
                        {
                            for (int a = 0; a < dt.Rows.Count; a++)
                            {
                                traseu = "";
                                string magistrale = dataGridView1.Rows[a].Cells[6].Value.ToString();
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
                                        cmd.CommandText = "select Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox2.Text + "%') and Statii.Statii LIKE ('%" + orase[j] + "%')";
                                        SqlDataAdapter da1 = new SqlDataAdapter(cmd);
                                        DataTable dt1 = new DataTable();
                                        da1.Fill(dt1);
                                        if (dt1.Rows.Count != 0)
                                        {
                                            intermediar = orase[j];
                                            verif = 1;
                                            break;
                                        }
                                    }
                                    if (verif == 1)
                                        break;
                                }
                                if (verif == 1)
                                    break;
                            }
                        }
                        //MessageBox.Show(intermediar);
                        //Pasul 2: Verifica directia trenului dintre orasul de plecare si locul intermediar    
                        cmd.CommandText = "select Curse.TipTren,Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii,Curse.Via,Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + comboBox1.Text + "%') AND Statii.Statii LIKE ('%" + intermediar + "%') ";
                        SqlDataAdapter da2 = new SqlDataAdapter(cmd);
                        DataTable dt2 = new DataTable();
                        da2.Fill(dt2);
                        dataGridView1.DataSource = dt2;
                        if (dt2.Rows.Count != 0)
                        {
                            for (int a = 0; a < dt2.Rows.Count; a++)
                            {
                                vector.Clear();
                                traseu1 = "";
                                string magistrale = dataGridView1.Rows[a].Cells[6].Value.ToString();
                                NumarTren1 = Convert.ToInt32(dataGridView1.Rows[a].Cells[1].Value.ToString());
                                TipTren1 = dataGridView1.Rows[a].Cells[0].Value.ToString();
                                via1 = dataGridView1.Rows[a].Cells[7].Value.ToString();
                                oper1 = dataGridView1.Rows[a].Cells[8].Value.ToString();
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
                                        vector.Add(orase[j]);
                                        if (orase[j] == comboBox1.Text || orase[j] == intermediar)
                                            traseu1 += orase[j].Trim().ToString();
                                    }
                                }
                                if ((comboBox1.Text + intermediar).ToString() == traseu1)
                                {
                                    break;
                                }
                            }

                        }

                        int index1 = vector.IndexOf(comboBox1.Text);
                        int index2 = vector.IndexOf(intermediar);
                        Nr_statii = (index2 - index1);
                        schimb = intermediar;

                        //MessageBox.Show(Nr_statii.ToString());
                        //Pasul 3: gaseste tren catre locul de destinatie din localitatea intermediara

                        index1 = 0;
                        index2 = 0;
                        SqlCommand cmd1 = con.CreateCommand();
                        cmd1.CommandText = "select Curse.TipTren,Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Statii.Statii, Curse.Via, Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Statii.Statii LIKE ('%" + intermediar + "%') AND Statii.Statii LIKE ('%" + comboBox2.Text + "%') ";
                        SqlDataAdapter da4 = new SqlDataAdapter(cmd1);
                        DataTable dt4 = new DataTable();
                        da4.Fill(dt4);
                        dataGridView1.DataSource = dt4;
                        if (dt4.Rows.Count != 0)
                        {
                            for (int a = 0; a < dt4.Rows.Count; a++)
                            {

                                vector.Clear();
                                traseu2 = "";
                                NumarTren2 = Convert.ToInt32(dataGridView1.Rows[a].Cells[1].Value.ToString());
                                TipTren2 = dataGridView1.Rows[a].Cells[0].Value.ToString();
                                string magistrale = dataGridView1.Rows[a].Cells[6].Value.ToString();
                                via1 = dataGridView1.Rows[a].Cells[7].Value.ToString();
                                oper2 = dataGridView1.Rows[a].Cells[7].Value.ToString();
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
                                        vector.Add(orase[j]);
                                        if (orase[j] == comboBox1.Text || orase[j] == intermediar)
                                            traseu2 += orase[j].Trim().ToString();
                                    }
                                }
                                if ((comboBox1.Text + intermediar).ToString() == traseu1)
                                {
                                    break;
                                }
                            }

                        }

                        index1 = vector.IndexOf(intermediar);
                        index2 = vector.IndexOf(comboBox2.Text);
                        Nr_statii = (index2 - index1);
                        //MessageBox.Show(Nr_statii.ToString());

                        //Pasul 4: afisare trenuri;
                        cmd.CommandText = "select Curse.TipTren,Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Curse.Via,Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Curse.NumarTren=('" + NumarTren1 + "') UNION select Curse.TipTren,Statii.NumarTren, Curse.LocPlecare, Curse.LocSosire, Curse.OraPlecare, Curse.OraSosire,Curse.Via,Curse.Operator from statii,curse where Statii.NumarTren = Curse.NumarTren and Curse.NumarTren=('" + NumarTren2 + "')";
                        SqlDataAdapter da5 = new SqlDataAdapter(cmd);
                        DataTable dt5 = new DataTable();
                        da5.Fill(dt5);
                        dataGridView1.DataSource = dt5;
                        //MessageBox.Show(Nr_statii.ToString());


                    }

                }
        }

                    #endregion Un schimb




        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            validare_cautare();
            validare();
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            validare_cautare();
            validare();
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex > -1)
            {
                SqlCommand cmd = con.CreateCommand();
                cmd.CommandText = "select * from statii where NumarTren=('" + dataGridView1.Rows[e.RowIndex].Cells[1].Value + "')";
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                dataGridView2.DataSource = dt;
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView1_DataSourceChanged(object sender, EventArgs e)
        {

        }
        private void validare()
        {
            if ((radioButton1.Checked == true || radioButton2.Checked == true || radioButton3.Checked == true || radioButton4.Checked == true || radioButton5.Checked == true || radioButton6.Checked == true) && (radioButton7.Checked == true || radioButton8.Checked == true) && (radioButton9.Checked == true || radioButton10.Checked == true || radioButton11.Checked == true || radioButton12.Checked == true || radioButton13.Checked == true) && ((String.IsNullOrEmpty(comboBox1.Text) == false && String.IsNullOrEmpty(comboBox2.Text) == false) && dataGridView1.DataSource != null))
            {
                button2.Enabled = true;
            }
            else
                button2.Enabled = false;
        }

        private void radioButton13_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton3_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton4_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton5_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton6_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton7_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton8_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton9_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton10_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton11_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void radioButton12_CheckedChanged(object sender, EventArgs e)
        {
            validare();
        }

        private void pnlAchizitonare_Paint(object sender, PaintEventArgs e)
        {

        }
        #region Calculare pret
        private void button2_Click(object sender, EventArgs e)
        {
            clasa = "";
            dusintors = "";
            varsta = "";
            button3.Enabled = true;
            SqlCommand cmd = con.CreateCommand();
            double pret = 0;
            if (radioButton1.Checked == true)
            {
                cmd.CommandText = "select * from preturi";
                using (SqlDataReader dr = cmd.ExecuteReader())
                {
                    while (dr.Read())
                    {
                        if (Convert.ToInt32(dr[0].ToString()) == NumarTren)
                        {
                            if (String.IsNullOrEmpty(dr[1].ToString()) == true)
                                MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                            else
                            {
                                pret += ((Convert.ToDouble(dr[1]) / Nr_statii_tot) * Nr_statii);
                            }
                        }
                        else
                        {
                            if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                if (String.IsNullOrEmpty(dr[1].ToString()) == true)
                                    MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                else
                                {
                                    pret += ((Convert.ToDouble(dr[1]) / Nr_statii_tot) * Nr_statii);
                                }
                            else
                                if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                    if (String.IsNullOrEmpty(dr[1].ToString()) == true)
                                        MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                    else
                                    {
                                        pret += ((Convert.ToDouble(dr[1]) / Nr_statii_tot) * Nr_statii);
                                    }
                        }
                    }
                }
                clasa = "Clasa 1";
                if (radioButton8.Checked == true)
                {
                    pret *= 2;
                    dusintors = "Dus - Intors";
                }
                if (radioButton7.Checked == true)
                {
                    dusintors = "Dus";

                }
                if (radioButton9.Checked == true)
                {
                    varsta = "Adult";
                }
                if (radioButton10.Checked == true)
                {
                    pret /= 2;
                    varsta = "Elev";
                }
                if (radioButton11.Checked == true)
                {
                    pret /= 2;
                    varsta = "Copil";
                }
                if (radioButton12.Checked == true)
                {
                    pret *= 0;
                    varsta = "Student";
                }
                if (radioButton13.Checked == true)
                {
                    pret /= 2;
                    varsta = "Pensionar";
                }
            }
            else
            {
                if (radioButton2.Checked == true)
                {
                    cmd.CommandText = "select * from preturi";
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {
                        while (dr.Read())
                        {
                            if (Convert.ToInt32(dr[0].ToString()) == NumarTren)
                            {
                                if (String.IsNullOrEmpty(dr[2].ToString()) == true)
                                    MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                else
                                {
                                    pret += ((Convert.ToDouble(dr[2]) / Nr_statii_tot) * Nr_statii);
                                }
                            }
                            else
                            {
                                if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                    if (String.IsNullOrEmpty(dr[2].ToString()) == true)
                                        MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                    else
                                    {
                                        pret += ((Convert.ToDouble(dr[2]) / Nr_statii_tot) * Nr_statii);
                                    }
                                else
                                    if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                        if (String.IsNullOrEmpty(dr[2].ToString()) == true)
                                            MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                        else
                                        {
                                            pret += ((Convert.ToDouble(dr[2]) / Nr_statii_tot) * Nr_statii);
                                        }
                            }
                        }
                    }
                    clasa = "Clasa a 2-a cu loc";
                    if (radioButton8.Checked == true)
                    {
                        pret *= 2;
                        dusintors = "Dus - Intors";
                    }
                    if (radioButton7.Checked == true)
                    {
                        dusintors = "Dus";

                    }
                    if (radioButton9.Checked == true)
                    {
                        varsta = "Adult";
                    }
                    if (radioButton10.Checked == true)
                    {
                        pret /= 2;
                        varsta = "Elev";
                    }
                    if (radioButton11.Checked == true)
                    {
                        pret /= 2;
                        varsta = "Copil";
                    }
                    if (radioButton12.Checked == true)
                    {
                        pret *= 0;
                        varsta = "Student";
                    }
                    if (radioButton13.Checked == true)
                    {
                        pret /= 2;
                        varsta = "Pensionar";
                    }
                }
                else
                {
                    if (radioButton3.Checked == true)
                    {
                        cmd.CommandText = "select * from preturi";
                        using (SqlDataReader dr = cmd.ExecuteReader())
                        {
                            while (dr.Read())
                            {
                                if (Convert.ToInt32(dr[0].ToString()) == NumarTren)
                                {
                                    if (String.IsNullOrEmpty(dr[3].ToString()) == true)
                                        MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                    else
                                    {
                                        pret += ((Convert.ToDouble(dr[3]) / Nr_statii_tot) * Nr_statii);
                                    }
                                }
                                else
                                {
                                    if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                        if (String.IsNullOrEmpty(dr[3].ToString()) == true)
                                            MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                        else
                                        {
                                            pret += ((Convert.ToDouble(dr[3]) / Nr_statii_tot) * Nr_statii);
                                        }
                                    else
                                        if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                            if (String.IsNullOrEmpty(dr[3].ToString()) == true)
                                                MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                            else
                                            {
                                                pret += ((Convert.ToDouble(dr[3]) / Nr_statii_tot) * Nr_statii);
                                            }
                                }
                            }
                        }
                        clasa = "Clasa a 2-a fara loc";
                        if (radioButton8.Checked == true)
                        {
                            pret *= 2;
                            dusintors = "Dus - Intors";
                        }
                        if (radioButton7.Checked == true)
                        {
                            dusintors = "Dus";

                        }
                        if (radioButton9.Checked == true)
                        {
                            varsta = "Adult";
                        }
                        if (radioButton10.Checked == true)
                        {
                            pret /= 2;
                            varsta = "Elev";
                        }
                        if (radioButton11.Checked == true)
                        {
                            pret /= 2;
                            varsta = "Copil";
                        }
                        if (radioButton12.Checked == true)
                        {
                            pret *= 0;
                            varsta = "Student";
                        }
                        if (radioButton13.Checked == true)
                        {
                            pret /= 2;
                            varsta = "Pensionar";
                        }
                    }
                    else
                    {
                        if (radioButton4.Checked == true)
                        {
                            cmd.CommandText = "select * from preturi";
                            using (SqlDataReader dr = cmd.ExecuteReader())
                            {
                                while (dr.Read())
                                {
                                    if (Convert.ToInt32(dr[0].ToString()) == NumarTren)
                                    {
                                        if (String.IsNullOrEmpty(dr[4].ToString()) == true)
                                            MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                        else
                                        {
                                            pret += ((Convert.ToDouble(dr[4]) / Nr_statii_tot) * Nr_statii);
                                        }
                                    }
                                    else
                                    {
                                        if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                            if (String.IsNullOrEmpty(dr[4].ToString()) == true)
                                                MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                            else
                                            {
                                                pret += ((Convert.ToDouble(dr[4]) / Nr_statii_tot) * Nr_statii);
                                            }
                                        else
                                            if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                                if (String.IsNullOrEmpty(dr[4].ToString()) == true)
                                                    MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                else
                                                {
                                                    pret += ((Convert.ToDouble(dr[4]) / Nr_statii_tot) * Nr_statii);
                                                }
                                    }
                                }
                            }
                            clasa = "Vagon cuseta";
                            if (radioButton8.Checked == true)
                            {
                                pret *= 2;
                                dusintors = "Dus - Intors";
                            }
                            if (radioButton7.Checked == true)
                            {
                                dusintors = "Dus";

                            }
                            if (radioButton9.Checked == true)
                            {
                                varsta = "Adult";
                            }
                            if (radioButton10.Checked == true)
                            {
                                pret /= 2;
                                varsta = "Elev";
                            }
                            if (radioButton11.Checked == true)
                            {
                                pret /= 2;
                                varsta = "Copil";
                            }
                            if (radioButton12.Checked == true)
                            {
                                pret *= 0;
                                varsta = "Student";
                            }
                            if (radioButton13.Checked == true)
                            {
                                pret /= 2;
                                varsta = "Pensionar";
                            }
                        }
                        else
                        {
                            if (radioButton5.Checked == true)
                            {
                                cmd.CommandText = "select * from preturi";
                                using (SqlDataReader dr = cmd.ExecuteReader())
                                {
                                    while (dr.Read())
                                    {
                                        if (Convert.ToInt32(dr[5].ToString()) == NumarTren)
                                        {
                                            if (String.IsNullOrEmpty(dr[5].ToString()) == true)
                                                MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                            else
                                            {
                                                pret += ((Convert.ToDouble(dr[5]) / Nr_statii_tot) * Nr_statii);
                                            }
                                        }
                                        else
                                        {
                                            if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                                if (String.IsNullOrEmpty(dr[4].ToString()) == true)
                                                    MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                else
                                                {
                                                    pret += ((Convert.ToDouble(dr[5]) / Nr_statii_tot) * Nr_statii);
                                                }
                                            else
                                                if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                                    if (String.IsNullOrEmpty(dr[5].ToString()) == true)
                                                        MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                    else
                                                    {
                                                        pret += ((Convert.ToDouble(dr[5]) / Nr_statii_tot) * Nr_statii);
                                                    }
                                        }
                                    }
                                }
                                clasa = "Vagon de dormit cu pat simplu";
                                if (radioButton8.Checked == true)
                                {
                                    pret *= 2;
                                    dusintors = "Dus - Intors";
                                }
                                if (radioButton7.Checked == true)
                                {
                                    dusintors = "Dus";

                                }
                                if (radioButton9.Checked == true)
                                {
                                    varsta = "Adult";
                                }
                                if (radioButton10.Checked == true)
                                {
                                    pret /= 2;
                                    varsta = "Elev";
                                }
                                if (radioButton11.Checked == true)
                                {
                                    pret /= 2;
                                    varsta = "Copil";
                                }
                                if (radioButton12.Checked == true)
                                {
                                    pret *= 0;
                                    varsta = "Student";
                                }
                                if (radioButton13.Checked == true)
                                {
                                    pret /= 2;
                                    varsta = "Pensionar";
                                }
                            }
                            else
                            {
                                if (radioButton6.Checked == true)
                                {
                                    cmd.CommandText = "select * from preturi";
                                    using (SqlDataReader dr = cmd.ExecuteReader())
                                    {
                                        while (dr.Read())
                                        {
                                            if (Convert.ToInt32(dr[0].ToString()) == NumarTren)
                                            {
                                                if (String.IsNullOrEmpty(dr[6].ToString()) == true)
                                                    MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                else
                                                {
                                                    pret += ((Convert.ToDouble(dr[6]) / Nr_statii_tot) * Nr_statii);
                                                }
                                            }
                                            else
                                            {
                                                if (Convert.ToInt32(dr[0].ToString()) == NumarTren1)
                                                    if (String.IsNullOrEmpty(dr[6].ToString()) == true)
                                                        MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                    else
                                                    {
                                                        pret += ((Convert.ToDouble(dr[6]) / Nr_statii_tot) * Nr_statii);
                                                    }
                                                else
                                                    if (Convert.ToInt32(dr[0].ToString()) == NumarTren2)
                                                        if (String.IsNullOrEmpty(dr[6].ToString()) == true)
                                                            MessageBox.Show("Nu puteti rezerva un bilet la aceasta clasa!");
                                                        else
                                                        {
                                                            pret += ((Convert.ToDouble(dr[6]) / Nr_statii_tot) * Nr_statii);
                                                        }
                                            }
                                        }
                                    }
                                    clasa = "Vagon de dormit cu pat dublu";
                                    if (radioButton8.Checked == true)
                                    {
                                        pret *= 2;
                                        dusintors = "Dus - Intors";
                                    }
                                    if (radioButton7.Checked == true)
                                    {
                                        dusintors = "Dus";

                                    }
                                    if (radioButton9.Checked == true)
                                    {
                                        varsta = "Adult";
                                    }
                                    if (radioButton10.Checked == true)
                                    {
                                        pret /= 2;
                                        varsta = "Elev";
                                    }
                                    if (radioButton11.Checked == true)
                                    {
                                        pret /= 2;
                                        varsta = "Copil";
                                    }
                                    if (radioButton12.Checked == true)
                                    {
                                        pret *= 0;
                                        varsta = "Student";
                                    }
                                    if (radioButton13.Checked == true)
                                    {
                                        pret /= 2;
                                        varsta = "Pensionar";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            label10.Text = pret.ToString() + " RON";
        }
        #endregion Calculare pret

        private void button3_Click(object sender, EventArgs e)
        {
            textBox1.AppendText("================================================ Bilet de calatorie ==========================================" + Environment.NewLine + Environment.NewLine);
            if (NumarTren != 0)
            {
                textBox1.AppendText("Numarul trenului:   " + TipTren.ToString() + " " + NumarTren.ToString() + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Data emiteri biletului:    " + DateTime.Now + Environment.NewLine);
                textBox1.AppendText("Punctul de plecare:    " + comboBox1.Text + Environment.NewLine);
                textBox1.AppendText("Punctul de sosire:    " + comboBox2.Text + Environment.NewLine);
                if (String.IsNullOrEmpty(via))
                    textBox1.AppendText("Via:    " + "-" + Environment.NewLine);
                else
                    textBox1.AppendText("Via:    " + via + Environment.NewLine);
                textBox1.AppendText("Clasa: " + clasa + Environment.NewLine);
                textBox1.AppendText("Tip bilet: " + dusintors + Environment.NewLine);
                textBox1.AppendText("Categorie de varsta: " + varsta + Environment.NewLine);
                textBox1.AppendText("Pret bilet: " + label10.Text + Environment.NewLine);
                textBox1.AppendText("Operator: " + oper + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Achizitia si controlul biletului cu pret redus se face pe baza de legitimare si dovada a aplicabilitatii reducerii!" + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Societatea Nationala a Cailor Ferate Romane va ureaza drum bun si calatorie placuta!");
                textBox1.AppendText(Environment.NewLine);

            }
            else
            {
                textBox1.AppendText("Numarul trenului:   " + TipTren1.ToString() + " " + NumarTren1.ToString() + Environment.NewLine);
                textBox1.AppendText("Data emiteri biletului:    " + DateTime.Now + Environment.NewLine);
                textBox1.AppendText("Punctul de plecare:    " + comboBox1.Text + Environment.NewLine);
                textBox1.AppendText("Punctul de sosire:    " + schimb.ToString() + Environment.NewLine);
                if (String.IsNullOrEmpty(via1))
                    textBox1.AppendText("Via:    " + "-" + Environment.NewLine);
                else
                    textBox1.AppendText("Via:    " + via1 + Environment.NewLine);
                textBox1.AppendText("Clasa: " + clasa + Environment.NewLine);
                textBox1.AppendText("Tip bilet: " + dusintors + Environment.NewLine);
                textBox1.AppendText("Categorie de varsta: " + varsta + Environment.NewLine);
                textBox1.AppendText("Pret bilet: " + label10.Text + Environment.NewLine);
                textBox1.AppendText("Operator: " + oper1 + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Achizitia si controlul biletului cu pret redus se face pe baza de legitimare si dovada a aplicabilitatii reducerii!" + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Societatea Nationala a Cailor Ferate Romane va ureaza drum bun si calatorie placuta!");
                textBox1.AppendText(Environment.NewLine + Environment.NewLine + Environment.NewLine);

                textBox1.AppendText("================================================ Bilet de calatorie ==========================================" + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Numarul trenului:   " + TipTren2.ToString() + " " + NumarTren2.ToString() + Environment.NewLine);
                textBox1.AppendText("Data emiteri biletului:    " + DateTime.Now + Environment.NewLine);
                textBox1.AppendText("Punctul de plecare:    " + schimb + Environment.NewLine);
                textBox1.AppendText("Punctul de sosire:    " + comboBox2.Text + Environment.NewLine);
                if (String.IsNullOrEmpty(via2))
                    textBox1.AppendText("Via:    " + "-" + Environment.NewLine);
                else
                    textBox1.AppendText("Via:    " + via2 + Environment.NewLine);
                textBox1.AppendText("Clasa: " + clasa + Environment.NewLine);
                textBox1.AppendText("Tip bilet: " + dusintors + Environment.NewLine);
                textBox1.AppendText("Categorie de varsta: " + varsta + Environment.NewLine);
                textBox1.AppendText("Pret bilet: " + label10.Text + Environment.NewLine);
                textBox1.AppendText("Operator: " + oper2 + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Achizitia si controlul biletului cu pret redus se face pe baza de legitimare si dovada a aplicabilitatii reducerii!" + Environment.NewLine + Environment.NewLine);
                textBox1.AppendText("Societatea Nationala a Cailor Ferate Romane va ureaza drum bun si calatorie placuta!");
                textBox1.AppendText(Environment.NewLine + Environment.NewLine + Environment.NewLine);
            }

        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            validare_cautare();
            validare_planificare();
        }
        void validare_planificare()
        {
            if (checkBox1.Checked == true)
            {
                comboBox3.Enabled = true;
            }
            else
            {
                comboBox3.Enabled = false;
                
            }

        }

        private void comboBox3_SelectedIndexChanged(object sender, EventArgs e)
        {
            validare_cautare();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            textBox1.Text = "";
        }

        private void btnAutentificare_Click(object sender, EventArgs e)
        {
            (new logare()).Show();
            this.Hide();
        }
    }
}
