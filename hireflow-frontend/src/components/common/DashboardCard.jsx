import { motion } from "framer-motion";

function DashboardCard({ title, value, color }) {
    return (
        <motion.div
            whileHover={{ scale: 1.05 }}
            className="card shadow-lg p-4 text-center"
            style={{
                borderRadius: "20px",
                background: color,
                color: "white"
            }}
        >
            <h5>{title}</h5>
            <h2>{value}</h2>
        </motion.div>
    );
}

export default DashboardCard;